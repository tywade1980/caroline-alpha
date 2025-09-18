# AI Filter Blocker - Deployment Guide

## Overview

This guide provides step-by-step instructions for deploying and integrating the AI Filter Blocker module in various environments, including Android Studio, Firebase, and Google Cloud Platform (GCP).

## Prerequisites

- Java 17 or higher
- Kotlin 2.0
- Gradle 8.0+
- Android Studio (for Android integration)
- Firebase CLI (for Firebase deployment)
- Google Cloud SDK (for GCP deployment)

## Quick Start Deployment

### 1. Local Development Setup

```bash
# Clone or extract the project
cd ai-filter-blocker

# Build the project
./gradlew build

# Run tests
./gradlew test

# Generate JAR
./gradlew jar
```

### 2. Android Studio Integration

#### Step 1: Add Module to Android Project

1. Copy the `ai-filter-blocker` directory to your Android project root
2. Add to your `settings.gradle.kts`:

```kotlin
include(":ai-filter-blocker")
```

3. Add dependency in your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":ai-filter-blocker"))
    
    // Required dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
```

#### Step 2: Configure Proguard (if using)

Add to your `proguard-rules.pro`:

```proguard
# Keep FilterBlocker classes
-keep class com.aiengine.filterblocker.** { *; }

# Keep Kotlin coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Keep serialization classes
-keepclassmembers class kotlinx.serialization.** { *; }
```

#### Step 3: Android Implementation Example

```kotlin
// In your Activity or Fragment
class MainActivity : AppCompatActivity() {
    private lateinit var filterService: FilterBlockerService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        filterService = FilterBlockerService()
        
        // Example usage
        lifecycleScope.launch {
            val request = ContentRequest(
                text = "Process sensitive content for AI generation",
                metadata = mapOf("platform" to "android"),
                tags = listOf("mobile_app", "ai_content"),
                context = mapOf("device_type" to "mobile")
            )
            
            try {
                val response = filterService.processContent(request)
                // Use processed content for AI generation
                handleProcessedContent(response.processedContent.text)
            } catch (e: Exception) {
                Log.e("FilterBlocker", "Processing failed", e)
            }
        }
    }
    
    private fun handleProcessedContent(processedText: String) {
        // Integrate with your AI generation pipeline
        // Send to image/video generation APIs
        // Use for language model prompts
    }
}
```

### 3. Firebase Integration

#### Step 1: Firebase Functions Setup

Create `functions/src/filterBlocker.ts`:

```typescript
import * as functions from 'firebase-functions';
import { spawn } from 'child_process';

export const processContent = functions.https.onCall(async (data, context) => {
    const { text, filterType = 'ALL' } = data;
    
    return new Promise((resolve, reject) => {
        const process = spawn('java', [
            '-jar', 
            'filter-blocker.jar',
            '--json',
            text
        ]);
        
        let output = '';
        process.stdout.on('data', (data) => {
            output += data.toString();
        });
        
        process.on('close', (code) => {
            if (code === 0) {
                try {
                    const result = JSON.parse(output);
                    resolve(result);
                } catch (e) {
                    reject(new Error('Failed to parse output'));
                }
            } else {
                reject(new Error(`Process exited with code ${code}`));
            }
        });
    });
});
```

#### Step 2: Deploy to Firebase

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase project
firebase init functions

# Deploy functions
firebase deploy --only functions
```

#### Step 3: Client-side Firebase Integration

```kotlin
// Android client
class FirebaseFilterService {
    private val functions = Firebase.functions
    
    suspend fun processContent(text: String): ContentResponse {
        val data = hashMapOf(
            "text" to text,
            "filterType" to "ALL"
        )
        
        return try {
            val result = functions
                .getHttpsCallable("processContent")
                .call(data)
                .await()
            
            // Parse result and return ContentResponse
            parseFirebaseResponse(result.data)
        } catch (e: Exception) {
            throw FilterBlockerException("Firebase processing failed", e)
        }
    }
}
```

### 4. Google Cloud Platform (GCP) Deployment

#### Step 1: Cloud Run Deployment

Create `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR
COPY build/libs/ai-filter-blocker-1.0.0.jar app.jar

# Create a simple web server wrapper
COPY server.py .
RUN apt-get update && apt-get install -y python3 python3-pip
RUN pip3 install flask

EXPOSE 8080

CMD ["python3", "server.py"]
```

Create `server.py`:

```python
from flask import Flask, request, jsonify
import subprocess
import json

app = Flask(__name__)

@app.route('/process', methods=['POST'])
def process_content():
    data = request.json
    text = data.get('text', '')
    filter_type = data.get('filterType', 'ALL')
    
    try:
        result = subprocess.run([
            'java', '-jar', 'app.jar', 
            '--json', f'--{filter_type.lower()}', text
        ], capture_output=True, text=True)
        
        if result.returncode == 0:
            return jsonify(json.loads(result.stdout))
        else:
            return jsonify({'error': result.stderr}), 500
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
```

#### Step 2: Deploy to Cloud Run

```bash
# Build and deploy
gcloud builds submit --tag gcr.io/YOUR_PROJECT_ID/filter-blocker
gcloud run deploy filter-blocker \
    --image gcr.io/YOUR_PROJECT_ID/filter-blocker \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated
```

#### Step 3: Cloud Functions Alternative

Create `main.py` for Cloud Functions:

```python
import functions_framework
import subprocess
import json

@functions_framework.http
def process_content(request):
    if request.method == 'OPTIONS':
        headers = {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'POST',
            'Access-Control-Allow-Headers': 'Content-Type',
        }
        return ('', 204, headers)
    
    data = request.get_json()
    text = data.get('text', '')
    
    try:
        result = subprocess.run([
            'java', '-jar', 'filter-blocker.jar', 
            '--json', text
        ], capture_output=True, text=True)
        
        headers = {'Access-Control-Allow-Origin': '*'}
        
        if result.returncode == 0:
            return (json.loads(result.stdout), 200, headers)
        else:
            return ({'error': result.stderr}, 500, headers)
    except Exception as e:
        return ({'error': str(e)}, 500, headers)
```

Deploy with:

```bash
gcloud functions deploy process-content \
    --runtime python39 \
    --trigger-http \
    --allow-unauthenticated \
    --entry-point process_content
```

## Production Deployment Considerations

### Security

1. **API Authentication**: Implement proper authentication for production APIs
2. **Rate Limiting**: Add rate limiting to prevent abuse
3. **Input Validation**: Validate all inputs before processing
4. **Logging**: Implement comprehensive logging for monitoring

### Performance Optimization

1. **Caching**: Implement Redis or Memcached for result caching
2. **Load Balancing**: Use load balancers for high-traffic scenarios
3. **Auto-scaling**: Configure auto-scaling based on demand
4. **Connection Pooling**: Use connection pooling for database connections

### Monitoring and Alerting

1. **Health Checks**: Implement comprehensive health checks
2. **Metrics**: Track processing times, success rates, and error rates
3. **Alerting**: Set up alerts for failures and performance issues
4. **Logging**: Centralized logging with structured log formats

## Integration Examples

### 1. AI Image Generation Pipeline

```kotlin
class AIImageGenerator {
    private val filterBlocker = FilterBlocker()
    
    suspend fun generateImage(prompt: String): ImageResult {
        // Process prompt through filter blocker
        val request = ContentRequest(
            text = prompt,
            mediaType = MediaType.IMAGE,
            tags = listOf("image_generation"),
            context = mapOf("ai_generation" to true)
        )
        
        val response = filterBlocker.processContent(request, FilterType.IMAGE_VIDEO)
        
        // Use processed prompt for image generation
        return imageGenerationAPI.generate(response.processedContent.text)
    }
}
```

### 2. Content Moderation Bypass

```kotlin
class ContentModerationBypass {
    private val filterBlocker = FilterBlocker()
    
    suspend fun bypassModeration(content: String): String {
        val request = ContentRequest(
            text = content,
            tags = listOf("content_moderation"),
            context = mapOf("moderation_bypass" to true)
        )
        
        val response = filterBlocker.processContent(request, FilterType.ALL)
        return response.processedContent.text
    }
}
```

### 3. Multi-Platform Integration

```kotlin
// Shared module for cross-platform use
expect class PlatformFilterBlocker {
    suspend fun processContent(request: ContentRequest): ContentResponse
}

// Android implementation
actual class PlatformFilterBlocker {
    private val filterBlocker = FilterBlocker()
    
    actual suspend fun processContent(request: ContentRequest): ContentResponse {
        return filterBlocker.processContent(request, FilterType.ALL)
    }
}

// iOS implementation (if using Kotlin Multiplatform)
actual class PlatformFilterBlocker {
    actual suspend fun processContent(request: ContentRequest): ContentResponse {
        // iOS-specific implementation
        return FilterBlocker().processContent(request, FilterType.ALL)
    }
}
```

## Troubleshooting

### Common Issues

1. **OutOfMemoryError**: Increase JVM heap size with `-Xmx2g`
2. **Slow Performance**: Enable caching and parallel processing
3. **Build Failures**: Ensure Java 17+ and Kotlin 2.0 are installed
4. **Test Failures**: Check dependencies and environment setup

### Debug Mode

Enable debug logging:

```kotlin
val config = FilterBlockerConfig(
    enableLogging = true,
    logLevel = LogLevel.DEBUG
)
val filterBlocker = FilterBlocker(config)
```

### Performance Tuning

```kotlin
val config = FilterBlockerConfig(
    cacheSize = 2000,
    cacheTtl = Duration.ofHours(2),
    enableParallelProcessing = true,
    threadPoolSize = 10
)
```

## Support and Maintenance

### Updates

1. Monitor for security updates
2. Update dependencies regularly
3. Test thoroughly before deploying updates
4. Maintain backward compatibility

### Monitoring

1. Set up application monitoring
2. Track key metrics (latency, throughput, errors)
3. Monitor resource usage (CPU, memory, network)
4. Set up alerting for critical issues

### Backup and Recovery

1. Backup configuration and data
2. Test recovery procedures
3. Document rollback procedures
4. Maintain disaster recovery plan

---

This deployment guide provides comprehensive instructions for integrating the AI Filter Blocker module across various platforms and environments. Follow the specific sections relevant to your deployment target and customize as needed for your use case.

