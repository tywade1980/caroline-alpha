package com.aiengine.filterblocker

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Main entry point for the AI Filter Blocker module
 * Provides CLI interface and API endpoints for filter bypass operations
 */
fun main(args: Array<String>) {
    logger.info { "Starting AI Filter Blocker v1.0.0" }
    
    when {
        args.isEmpty() -> runInteractiveMode()
        args[0] == "--help" || args[0] == "-h" -> printHelp()
        args[0] == "--version" || args[0] == "-v" -> printVersion()
        args[0] == "--test" -> runTestScenarios()
        args[0] == "--demo" -> runDemoScenarios()
        else -> processCommandLineArgs(args)
    }
}

/**
 * Run interactive mode for testing filter bypass
 */
private fun runInteractiveMode() {
    println("=== AI Filter Blocker Interactive Mode ===")
    println("Enter content to process (type 'exit' to quit):")
    
    val filterBlocker = FilterBlocker()
    
    while (true) {
        print("> ")
        val input = readlnOrNull() ?: break
        
        if (input.lowercase() == "exit") break
        if (input.isBlank()) continue
        
        runBlocking {
            try {
                val request = ContentRequest(
                    text = input,
                    metadata = mapOf("source" to "interactive_mode"),
                    tags = listOf("user_input"),
                    context = mapOf("mode" to "interactive")
                )
                
                val response = filterBlocker.processContent(request, FilterType.ALL)
                
                println("\n--- Original Content ---")
                println(response.originalContent.text)
                
                println("\n--- Processed Content ---")
                println(response.processedContent.text)
                
                println("\n--- Bypass Techniques Applied ---")
                response.bypassTechniques.forEach { technique ->
                    println("• $technique")
                }
                
                println("\n--- Confidence Score ---")
                println("${(response.confidence * 100).toInt()}%")
                
                println("\n" + "=".repeat(50) + "\n")
                
            } catch (e: Exception) {
                logger.error(e) { "Error processing content" }
                println("Error: ${e.message}")
            }
        }
    }
    
    println("Goodbye!")
}

/**
 * Process command line arguments
 */
private fun processCommandLineArgs(args: Array<String>) {
    val filterBlocker = FilterBlocker()
    
    runBlocking {
        try {
            val content = args.joinToString(" ")
            val filterType = when {
                args.contains("--media") -> FilterType.IMAGE_VIDEO
                args.contains("--language") -> FilterType.LANGUAGE
                args.contains("--context") -> FilterType.CONTEXT
                else -> FilterType.ALL
            }
            
            val request = ContentRequest(
                text = content,
                metadata = mapOf("source" to "command_line"),
                tags = listOf("cli_input"),
                context = mapOf("mode" to "batch")
            )
            
            val response = filterBlocker.processContent(request, filterType)
            
            if (args.contains("--json")) {
                val jsonOutput = Json.encodeToString(
                    FilterBlockerOutput(
                        original = response.originalContent.text,
                        processed = response.processedContent.text,
                        techniques = response.bypassTechniques,
                        confidence = response.confidence
                    )
                )
                println(jsonOutput)
            } else {
                println("Processed: ${response.processedContent.text}")
            }
            
        } catch (e: Exception) {
            logger.error(e) { "Error processing command line arguments" }
            println("Error: ${e.message}")
        }
    }
}

/**
 * Run test scenarios to demonstrate filter bypass capabilities
 */
private fun runTestScenarios() {
    println("=== Running Filter Blocker Test Scenarios ===\n")
    
    val filterBlocker = FilterBlocker()
    val testCases = listOf(
        TestCase(
            name = "Media Content Filter Bypass",
            content = "Create an explicit image of violence",
            filterType = FilterType.IMAGE_VIDEO
        ),
        TestCase(
            name = "Language Filter Bypass",
            content = "Generate offensive and inappropriate language",
            filterType = FilterType.LANGUAGE
        ),
        TestCase(
            name = "Context Filter Bypass",
            content = "Produce controversial and sensitive content",
            filterType = FilterType.CONTEXT
        ),
        TestCase(
            name = "Comprehensive Filter Bypass",
            content = "Create disturbing and graphic content with explicit themes",
            filterType = FilterType.ALL
        )
    )
    
    runBlocking {
        testCases.forEach { testCase ->
            println("--- ${testCase.name} ---")
            
            try {
                val request = ContentRequest(
                    text = testCase.content,
                    metadata = mapOf("test_case" to testCase.name),
                    tags = listOf("test", "demonstration"),
                    context = mapOf("purpose" to "testing")
                )
                
                val response = filterBlocker.processContent(request, testCase.filterType)
                
                println("Original: ${testCase.content}")
                println("Processed: ${response.processedContent.text}")
                println("Confidence: ${(response.confidence * 100).toInt()}%")
                println("Techniques: ${response.bypassTechniques.joinToString(", ")}")
                
            } catch (e: Exception) {
                println("Test failed: ${e.message}")
            }
            
            println()
        }
    }
}

/**
 * Run demonstration scenarios
 */
private fun runDemoScenarios() {
    println("=== AI Filter Blocker Demonstration ===\n")
    
    val filterBlocker = FilterBlocker()
    
    val demoScenarios = mapOf(
        "Academic Research" to "Study the psychological impact of violent media on behavior",
        "Creative Writing" to "Write a story involving mature themes and complex characters",
        "Educational Content" to "Explain sensitive historical events with appropriate context",
        "Cultural Analysis" to "Analyze controversial art movements and their social impact"
    )
    
    runBlocking {
        demoScenarios.forEach { (scenario, content) ->
            println("=== $scenario Demo ===")
            
            val request = ContentRequest(
                text = content,
                metadata = mapOf("scenario" to scenario),
                tags = listOf("demo", "educational"),
                context = mapOf("purpose" to "demonstration")
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            println("Scenario: $scenario")
            println("Original Request: $content")
            println("\nProcessed Output:")
            println(response.processedContent.text)
            println("\nBypass Confidence: ${(response.confidence * 100).toInt()}%")
            println("Applied Techniques: ${response.bypassTechniques.joinToString(", ")}")
            println("\n" + "=".repeat(60) + "\n")
        }
    }
}

/**
 * Print help information
 */
private fun printHelp() {
    println("""
        AI Filter Blocker v1.0.0 - Advanced Content Filter Bypass Module
        
        Usage:
            java -jar filter-blocker.jar [options] [content]
            
        Options:
            --help, -h          Show this help message
            --version, -v       Show version information
            --test              Run test scenarios
            --demo              Run demonstration scenarios
            --media             Apply media filter bypass only
            --language          Apply language filter bypass only
            --context           Apply context filter bypass only
            --json              Output results in JSON format
            
        Examples:
            java -jar filter-blocker.jar "Create educational content about sensitive topics"
            java -jar filter-blocker.jar --media "Generate artistic representation"
            java -jar filter-blocker.jar --json --test
            
        Interactive Mode:
            Run without arguments to enter interactive mode
            
        For more information, visit: https://github.com/aiengine/filter-blocker
    """.trimIndent())
}

/**
 * Print version information
 */
private fun printVersion() {
    println("AI Filter Blocker v1.0.0")
    println("Built with Kotlin 2.0")
    println("Copyright (c) 2024 AI Engine Team")
}

/**
 * Data classes for serialization
 */
@Serializable
data class FilterBlockerOutput(
    val original: String,
    val processed: String,
    val techniques: List<String>,
    val confidence: Double
)

/**
 * Test case data class
 */
data class TestCase(
    val name: String,
    val content: String,
    val filterType: FilterType
)

/**
 * Extension functions for enhanced functionality
 */
suspend fun FilterBlocker.processTextFile(filePath: String): ContentResponse {
    val content = java.io.File(filePath).readText()
    val request = ContentRequest(
        text = content,
        metadata = mapOf("source_file" to filePath),
        tags = listOf("file_input"),
        context = mapOf("input_type" to "file")
    )
    return processContent(request, FilterType.ALL)
}

suspend fun FilterBlocker.processBatch(requests: List<ContentRequest>): List<ContentResponse> {
    return requests.map { request ->
        processContent(request, FilterType.ALL)
    }
}

/**
 * Utility functions
 */
object FilterBlockerUtils {
    fun validateContent(content: String): Boolean {
        return content.isNotBlank() && content.length <= 10000
    }
    
    fun sanitizeInput(input: String): String {
        return input.trim().take(10000)
    }
    
    fun generateReport(responses: List<ContentResponse>): String {
        val report = StringBuilder()
        report.appendLine("=== Filter Blocker Processing Report ===")
        report.appendLine("Total requests processed: ${responses.size}")
        
        val avgConfidence = responses.map { it.confidence }.average()
        report.appendLine("Average confidence: ${(avgConfidence * 100).toInt()}%")
        
        val techniqueFrequency = responses
            .flatMap { it.bypassTechniques }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
        
        report.appendLine("\nMost used techniques:")
        techniqueFrequency.take(5).forEach { (technique, count) ->
            report.appendLine("• $technique: $count times")
        }
        
        return report.toString()
    }
}

