package com.aireceptionist.app.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Utility class for audio recording and processing
 */
object AudioUtils {
    private const val SAMPLE_RATE = 16000
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    
    /**
     * Records audio from the microphone and saves it to a file
     * 
     * @param context The application context
     * @param maxDurationMs Maximum recording duration in milliseconds
     * @return Flow emitting the recording progress (0.0-1.0) and final file path
     */
    fun recordAudio(context: Context, maxDurationMs: Int): Flow<Pair<Float, String?>> = flow {
        var audioRecord: AudioRecord? = null
        var outputFile: File? = null
        var outputStream: FileOutputStream? = null
        
        try {
            val bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT
            )
            
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                throw IOException("Failed to get minimum buffer size for AudioRecord")
            }
            
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize
            )
            
            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
                throw IOException("AudioRecord failed to initialize")
            }
            
            // Create output file
            outputFile = File(context.cacheDir, "recording_${System.currentTimeMillis()}.pcm")
            outputStream = FileOutputStream(outputFile)
            
            val buffer = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .array()
            
            audioRecord.startRecording()
            
            val startTime = System.currentTimeMillis()
            var bytesRead: Int
            var totalBytesRead = 0L
            
            // Calculate total expected bytes
            val bytesPerSample = 2 // 16-bit PCM
            val bytesPerSecond = SAMPLE_RATE * bytesPerSample
            val totalExpectedBytes = bytesPerSecond * (maxDurationMs / 1000.0)
            
            while (System.currentTimeMillis() - startTime < maxDurationMs) {
                bytesRead = audioRecord.read(buffer, 0, buffer.size)
                
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead
                    
                    // Emit progress
                    val progress = (totalBytesRead / totalExpectedBytes).toFloat()
                    emit(Pair(progress.coerceIn(0f, 1f), null))
                }
            }
            
            // Cleanup
            audioRecord.stop()
            outputStream.close()
            
            // Convert PCM to WAV
            val wavFile = convertPcmToWav(context, outputFile)
            
            // Emit final result with file path
            emit(Pair(1f, wavFile.absolutePath))
            
        } catch (e: Exception) {
            Timber.e(e, "Error recording audio")
            emit(Pair(0f, null))
        } finally {
            try {
                audioRecord?.release()
                outputStream?.close()
            } catch (e: Exception) {
                Timber.e(e, "Error cleaning up audio recording resources")
            }
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Converts a PCM file to WAV format
     * 
     * @param context The application context
     * @param pcmFile The PCM file to convert
     * @return The converted WAV file
     */
    private fun convertPcmToWav(context: Context, pcmFile: File): File {
        val wavFile = File(context.cacheDir, pcmFile.nameWithoutExtension + ".wav")
        
        try {
            val pcmData = pcmFile.readBytes()
            val wavData = ByteArray(pcmData.size + 44) // WAV header is 44 bytes
            
            // WAV header
            // RIFF header
            wavData[0] = 'R'.code.toByte()
            wavData[1] = 'I'.code.toByte()
            wavData[2] = 'F'.code.toByte()
            wavData[3] = 'F'.code.toByte()
            
            // Chunk size (file size - 8)
            val fileSize = pcmData.size + 36
            wavData[4] = (fileSize and 0xFF).toByte()
            wavData[5] = (fileSize shr 8 and 0xFF).toByte()
            wavData[6] = (fileSize shr 16 and 0xFF).toByte()
            wavData[7] = (fileSize shr 24 and 0xFF).toByte()
            
            // WAVE header
            wavData[8] = 'W'.code.toByte()
            wavData[9] = 'A'.code.toByte()
            wavData[10] = 'V'.code.toByte()
            wavData[11] = 'E'.code.toByte()
            
            // fmt chunk
            wavData[12] = 'f'.code.toByte()
            wavData[13] = 'm'.code.toByte()
            wavData[14] = 't'.code.toByte()
            wavData[15] = ' '.code.toByte()
            
            // Subchunk1 size (16 for PCM)
            wavData[16] = 16
            wavData[17] = 0
            wavData[18] = 0
            wavData[19] = 0
            
            // Audio format (1 for PCM)
            wavData[20] = 1
            wavData[21] = 0
            
            // Number of channels (1 for mono)
            wavData[22] = 1
            wavData[23] = 0
            
            // Sample rate
            wavData[24] = (SAMPLE_RATE and 0xFF).toByte()
            wavData[25] = (SAMPLE_RATE shr 8 and 0xFF).toByte()
            wavData[26] = (SAMPLE_RATE shr 16 and 0xFF).toByte()
            wavData[27] = (SAMPLE_RATE shr 24 and 0xFF).toByte()
            
            // Byte rate (SampleRate * NumChannels * BitsPerSample/8)
            val byteRate = SAMPLE_RATE * 1 * 16 / 8
            wavData[28] = (byteRate and 0xFF).toByte()
            wavData[29] = (byteRate shr 8 and 0xFF).toByte()
            wavData[30] = (byteRate shr 16 and 0xFF).toByte()
            wavData[31] = (byteRate shr 24 and 0xFF).toByte()
            
            // Block align (NumChannels * BitsPerSample/8)
            wavData[32] = (1 * 16 / 8).toByte()
            wavData[33] = 0
            
            // Bits per sample
            wavData[34] = 16
            wavData[35] = 0
            
            // data chunk
            wavData[36] = 'd'.code.toByte()
            wavData[37] = 'a'.code.toByte()
            wavData[38] = 't'.code.toByte()
            wavData[39] = 'a'.code.toByte()
            
            // Subchunk2 size (data size)
            wavData[40] = (pcmData.size and 0xFF).toByte()
            wavData[41] = (pcmData.size shr 8 and 0xFF).toByte()
            wavData[42] = (pcmData.size shr 16 and 0xFF).toByte()
            wavData[43] = (pcmData.size shr 24 and 0xFF).toByte()
            
            // Copy PCM data
            System.arraycopy(pcmData, 0, wavData, 44, pcmData.size)
            
            // Write WAV file
            wavFile.writeBytes(wavData)
            
            // Delete original PCM file
            pcmFile.delete()
            
            return wavFile
        } catch (e: Exception) {
            Timber.e(e, "Error converting PCM to WAV")
            throw e
        }
    }
    
    /**
     * Sets up audio attributes for playback
     * 
     * @return AudioAttributes for voice playback
     */
    fun getPlaybackAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
    }
}
