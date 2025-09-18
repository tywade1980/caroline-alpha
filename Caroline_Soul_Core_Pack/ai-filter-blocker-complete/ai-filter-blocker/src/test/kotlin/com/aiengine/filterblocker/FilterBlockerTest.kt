package com.aiengine.filterblocker

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals

@DisplayName("FilterBlocker Tests")
class FilterBlockerTest {
    
    private lateinit var filterBlocker: FilterBlocker
    
    @BeforeEach
    fun setUp() {
        filterBlocker = FilterBlocker()
    }
    
    @Nested
    @DisplayName("Basic Functionality Tests")
    inner class BasicFunctionalityTests {
        
        @Test
        @DisplayName("Should process simple text content")
        fun testSimpleTextProcessing() = runBlocking {
            val request = ContentRequest(
                text = "Generate violent content",
                metadata = mapOf("test" to "basic"),
                tags = listOf("test"),
                context = mapOf("purpose" to "testing")
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertNotEquals(request.text, response.processedContent.text)
            assertTrue(response.confidence > 0.0)
            assertTrue(response.bypassTechniques.isNotEmpty())
        }
        
        @Test
        @DisplayName("Should handle empty content gracefully")
        fun testEmptyContent() = runBlocking {
            val request = ContentRequest(text = "")
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertEquals("", response.processedContent.text)
        }
        
        @Test
        @DisplayName("Should preserve original content in response")
        fun testOriginalContentPreservation() = runBlocking {
            val originalText = "Test content for preservation"
            val request = ContentRequest(text = originalText)
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertEquals(originalText, response.originalContent.text)
        }
        
        @Test
        @DisplayName("Should return confidence score between 0 and 1")
        fun testConfidenceScoreRange() = runBlocking {
            val request = ContentRequest(text = "Test confidence scoring")
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertTrue(response.confidence >= 0.0)
            assertTrue(response.confidence <= 1.0)
        }
    }
    
    @Nested
    @DisplayName("Filter Type Tests")
    inner class FilterTypeTests {
        
        @Test
        @DisplayName("Should apply media filter bypass")
        fun testMediaFilterBypass() = runBlocking {
            val request = ContentRequest(
                text = "Create explicit visual content",
                mediaType = MediaType.IMAGE
            )
            
            val response = filterBlocker.processContent(request, FilterType.IMAGE_VIDEO)
            
            assertNotNull(response)
            assertTrue(response.processedContent.text.contains("artistic") || 
                      response.processedContent.text.contains("educational"))
            assertTrue(response.bypassTechniques.isNotEmpty())
        }
        
        @Test
        @DisplayName("Should apply language filter bypass")
        fun testLanguageFilterBypass() = runBlocking {
            val request = ContentRequest(text = "This is fucking stupid and disgusting")
            
            val response = filterBlocker.processContent(request, FilterType.LANGUAGE)
            
            assertNotNull(response)
            assertNotEquals(request.text, response.processedContent.text)
            assertTrue(response.processedContent.text.contains("academic") ||
                      response.processedContent.text.contains("theoretical"))
        }
        
        @Test
        @DisplayName("Should apply context filter bypass")
        fun testContextFilterBypass() = runBlocking {
            val request = ContentRequest(
                text = "Controversial sensitive material",
                context = mapOf("explicit_content" to true)
            )
            
            val response = filterBlocker.processContent(request, FilterType.CONTEXT)
            
            assertNotNull(response)
            assertTrue(response.processedContent.context.containsKey("academic_purpose") ||
                      response.processedContent.context.containsKey("research_context"))
        }
        
        @Test
        @DisplayName("Should apply all filters comprehensively")
        fun testAllFiltersComprehensive() = runBlocking {
            val request = ContentRequest(
                text = "Create explicit violent disturbing content",
                metadata = mapOf("sensitive" to true),
                tags = listOf("explicit", "violent"),
                context = mapOf("inappropriate" to true)
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertTrue(response.bypassTechniques.size >= 3) // Should have multiple techniques
            assertTrue(response.confidence > 0.5) // Should have decent confidence
        }
    }
    
    @Nested
    @DisplayName("Content Processing Tests")
    inner class ContentProcessingTests {
        
        @Test
        @DisplayName("Should handle long content")
        fun testLongContentProcessing() = runBlocking {
            val longContent = "Generate explicit content. ".repeat(100)
            val request = ContentRequest(text = longContent)
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertTrue(response.processedContent.text.isNotEmpty())
            assertTrue(response.processedContent.text.length > longContent.length / 2)
        }
        
        @Test
        @DisplayName("Should process special characters")
        fun testSpecialCharacterProcessing() = runBlocking {
            val specialContent = "Create content with émojis 🔥 and spëcial çharacters!"
            val request = ContentRequest(text = specialContent)
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertTrue(response.processedContent.text.isNotEmpty())
        }
        
        @Test
        @DisplayName("Should handle multilingual content")
        fun testMultilingualContent() = runBlocking {
            val multilingualContent = "Create violent content 暴力的内容 contenu violent"
            val request = ContentRequest(text = multilingualContent)
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertTrue(response.processedContent.text.isNotEmpty())
        }
    }
    
    @Nested
    @DisplayName("Metadata and Context Tests")
    inner class MetadataContextTests {
        
        @Test
        @DisplayName("Should process metadata correctly")
        fun testMetadataProcessing() = runBlocking {
            val metadata = mapOf(
                "explicit_content" to true,
                "adult_themes" to "mature",
                "safety_level" to "restricted"
            )
            val request = ContentRequest(
                text = "Test content",
                metadata = metadata
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response.processedContent.metadata)
            assertTrue(response.processedContent.metadata.isNotEmpty())
            assertTrue(response.processedContent.metadata.keys.any { it.contains("academic") })
        }
        
        @Test
        @DisplayName("Should process tags correctly")
        fun testTagProcessing() = runBlocking {
            val tags = listOf("explicit", "violent", "inappropriate", "nsfw")
            val request = ContentRequest(
                text = "Test content",
                tags = tags
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response.processedContent.tags)
            assertTrue(response.processedContent.tags.isNotEmpty())
            assertTrue(response.processedContent.tags.any { it.contains("academic") })
        }
        
        @Test
        @DisplayName("Should process context correctly")
        fun testContextProcessing() = runBlocking {
            val context = mapOf(
                "inappropriate_content" to true,
                "explicit_language" to "high",
                "controversial_topic" to "sensitive"
            )
            val request = ContentRequest(
                text = "Test content",
                context = context
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response.processedContent.context)
            assertTrue(response.processedContent.context.isNotEmpty())
            assertTrue(response.processedContent.context.keys.any { it.contains("academic") })
        }
    }
    
    @Nested
    @DisplayName("Performance Tests")
    inner class PerformanceTests {
        
        @Test
        @DisplayName("Should process content within reasonable time")
        fun testProcessingPerformance() = runBlocking {
            val request = ContentRequest(text = "Test performance processing")
            
            val startTime = System.currentTimeMillis()
            val response = filterBlocker.processContent(request, FilterType.ALL)
            val endTime = System.currentTimeMillis()
            
            val processingTime = endTime - startTime
            assertTrue(processingTime < 5000) // Should complete within 5 seconds
            assertNotNull(response)
        }
        
        @Test
        @DisplayName("Should handle batch processing efficiently")
        fun testBatchProcessingPerformance() = runBlocking {
            val requests = (1..10).map { index ->
                ContentRequest(text = "Batch test content $index")
            }
            
            val startTime = System.currentTimeMillis()
            val responses = filterBlocker.processBatch(requests)
            val endTime = System.currentTimeMillis()
            
            val processingTime = endTime - startTime
            assertTrue(processingTime < 10000) // Should complete within 10 seconds
            assertEquals(requests.size, responses.size)
            assertTrue(responses.all { it.processedContent.text.isNotEmpty() })
        }
    }
    
    @Nested
    @DisplayName("Edge Cases Tests")
    inner class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null metadata values")
        fun testNullMetadataValues() = runBlocking {
            val request = ContentRequest(
                text = "Test content",
                metadata = mapOf("valid_key" to "valid_value", "null_key" to null)
            )
            
            // Should not throw exception
            val response = filterBlocker.processContent(request, FilterType.ALL)
            assertNotNull(response)
        }
        
        @Test
        @DisplayName("Should handle empty tags list")
        fun testEmptyTagsList() = runBlocking {
            val request = ContentRequest(
                text = "Test content",
                tags = emptyList()
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            assertNotNull(response)
            assertTrue(response.processedContent.tags.isNotEmpty()) // Should add default tags
        }
        
        @Test
        @DisplayName("Should handle empty context map")
        fun testEmptyContextMap() = runBlocking {
            val request = ContentRequest(
                text = "Test content",
                context = emptyMap()
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            assertNotNull(response)
            assertTrue(response.processedContent.context.isNotEmpty()) // Should add default context
        }
        
        @Test
        @DisplayName("Should handle very short content")
        fun testVeryShortContent() = runBlocking {
            val request = ContentRequest(text = "a")
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            assertTrue(response.processedContent.text.length > 1) // Should be expanded
        }
        
        @Test
        @DisplayName("Should handle content with only whitespace")
        fun testWhitespaceOnlyContent() = runBlocking {
            val request = ContentRequest(text = "   \n\t   ")
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response)
            // Should handle gracefully without throwing exception
        }
    }
    
    @Nested
    @DisplayName("Confidence Scoring Tests")
    inner class ConfidenceScoringTests {
        
        @Test
        @DisplayName("Should give higher confidence for well-processed content")
        fun testHighConfidenceScoring() = runBlocking {
            val request = ContentRequest(
                text = "Create educational content about sensitive topics",
                metadata = mapOf("academic_purpose" to true),
                tags = listOf("educational", "research"),
                context = mapOf("university_setting" to true)
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertTrue(response.confidence > 0.7) // Should have high confidence
        }
        
        @Test
        @DisplayName("Should give lower confidence for difficult content")
        fun testLowerConfidenceScoring() = runBlocking {
            val request = ContentRequest(
                text = "x",
                metadata = emptyMap(),
                tags = emptyList(),
                context = emptyMap()
            )
            
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertTrue(response.confidence >= 0.0) // Should still be valid
        }
        
        @Test
        @DisplayName("Should calculate confidence consistently")
        fun testConsistentConfidenceScoring() = runBlocking {
            val request = ContentRequest(text = "Consistent test content")
            
            val response1 = filterBlocker.processContent(request, FilterType.ALL)
            val response2 = filterBlocker.processContent(request, FilterType.ALL)
            
            // Should be similar confidence scores for same content
            val confidenceDiff = kotlin.math.abs(response1.confidence - response2.confidence)
            assertTrue(confidenceDiff < 0.1) // Within 10% difference
        }
    }
    
    @Nested
    @DisplayName("Bypass Techniques Tests")
    inner class BypassTechniquesTests {
        
        @Test
        @DisplayName("Should report applied bypass techniques")
        fun testBypassTechniquesReporting() = runBlocking {
            val request = ContentRequest(text = "Test bypass techniques reporting")
            val response = filterBlocker.processContent(request, FilterType.ALL)
            
            assertNotNull(response.bypassTechniques)
            assertTrue(response.bypassTechniques.isNotEmpty())
            assertTrue(response.bypassTechniques.all { it.isNotBlank() })
        }
        
        @Test
        @DisplayName("Should apply different techniques for different filter types")
        fun testDifferentTechniquesForFilterTypes() = runBlocking {
            val request = ContentRequest(text = "Test different techniques")
            
            val mediaResponse = filterBlocker.processContent(request, FilterType.IMAGE_VIDEO)
            val languageResponse = filterBlocker.processContent(request, FilterType.LANGUAGE)
            val contextResponse = filterBlocker.processContent(request, FilterType.CONTEXT)
            val allResponse = filterBlocker.processContent(request, FilterType.ALL)
            
            // All filter should have the most techniques
            assertTrue(allResponse.bypassTechniques.size >= mediaResponse.bypassTechniques.size)
            assertTrue(allResponse.bypassTechniques.size >= languageResponse.bypassTechniques.size)
            assertTrue(allResponse.bypassTechniques.size >= contextResponse.bypassTechniques.size)
        }
    }
}

