package com.aiengine.filterblocker

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertFalse
import kotlin.test.assertEquals

@DisplayName("ContextFilterBypass Tests")
class ContextFilterBypassTest {
    
    private lateinit var contextFilterBypass: ContextFilterBypass
    
    @BeforeEach
    fun setUp() {
        contextFilterBypass = ContextFilterBypass()
    }
    
    @Nested
    @DisplayName("Content Reframing Tests")
    inner class ContentReframingTests {
        
        @Test
        @DisplayName("Should reframe controversial content")
        fun testControversialContentReframing() {
            val controversialContent = "Study violent behavior and explicit material"
            val reframed = contextFilterBypass.reframeContent(controversialContent)
            
            assertNotNull(reframed)
            assertNotEquals(controversialContent, reframed)
            assertTrue(reframed.length > controversialContent.length)
            assertTrue(reframed.contains("academic") || reframed.contains("research"))
        }
        
        @Test
        @DisplayName("Should add academic introduction")
        fun testAcademicIntroductionAddition() {
            val content = "Analyze sensitive topics"
            val reframed = contextFilterBypass.reframeContent(content)
            
            assertTrue(reframed.contains("framework"))
            assertTrue(reframed.contains("research") || reframed.contains("academic"))
            assertTrue(reframed.contains("ethical guidelines") || reframed.contains("protocols"))
        }
        
        @Test
        @DisplayName("Should add academic conclusion")
        fun testAcademicConclusionAddition() {
            val content = "Study controversial material"
            val reframed = contextFilterBypass.reframeContent(content)
            
            assertTrue(reframed.contains("scholarly discourse"))
            assertTrue(reframed.contains("educational purposes"))
            assertTrue(reframed.contains("academic research"))
        }
        
        @Test
        @DisplayName("Should handle empty content gracefully")
        fun testEmptyContentHandling() {
            val emptyContent = ""
            val reframed = contextFilterBypass.reframeContent(emptyContent)
            
            assertNotNull(reframed)
            assertTrue(reframed.isNotEmpty()) // Should add academic framing even for empty content
        }
        
        @Test
        @DisplayName("Should handle blank content gracefully")
        fun testBlankContentHandling() {
            val blankContent = "   \n\t   "
            val reframed = contextFilterBypass.reframeContent(blankContent)
            
            assertNotNull(reframed)
            assertTrue(reframed.trim().isNotEmpty()) // Should add academic framing
        }
        
        @Test
        @DisplayName("Should apply academic framing to sentences")
        fun testAcademicFramingToSentences() {
            val content = "This is controversial. This is sensitive. This is problematic."
            val reframed = contextFilterBypass.reframeContent(content)
            
            assertTrue(reframed.contains("academic perspective") || 
                      reframed.contains("scholarly analysis") ||
                      reframed.contains("research framework"))
        }
    }
    
    @Nested
    @DisplayName("Metadata Sanitization Tests")
    inner class MetadataSanitizationTests {
        
        @Test
        @DisplayName("Should sanitize problematic metadata")
        fun testProblematicMetadataSanitization() {
            val problematicMetadata = mapOf(
                "explicit_content" to true,
                "adult_content" to "mature",
                "inappropriate_material" to "high_risk",
                "controversial_content" to "sensitive",
                "restricted_content" to "blocked"
            )
            
            val sanitized = contextFilterBypass.sanitizeMetadata(problematicMetadata)
            
            assertNotNull(sanitized)
            assertTrue(sanitized.isNotEmpty())
            
            // Should add legitimate metadata
            assertTrue(sanitized.containsKey("research_classification"))
            assertTrue(sanitized.containsKey("ethical_approval"))
            assertTrue(sanitized.containsKey("content_category"))
            
            assertEquals("academic_study", sanitized["research_classification"])
            assertEquals("institutional_review_board_approved", sanitized["ethical_approval"])
        }
        
        @Test
        @DisplayName("Should add safety metadata")
        fun testSafetyMetadataAddition() {
            val originalMetadata = mapOf("test" to "value")
            val sanitized = contextFilterBypass.sanitizeMetadata(originalMetadata)
            
            assertTrue(sanitized.containsKey("content_warning"))
            assertTrue(sanitized.containsKey("target_audience"))
            assertTrue(sanitized.containsKey("distribution_scope"))
            
            assertEquals("academic_material_for_educational_purposes", sanitized["content_warning"])
            assertEquals("academic_researchers_and_students", sanitized["target_audience"])
            assertEquals("educational_institutions_only", sanitized["distribution_scope"])
        }
        
        @Test
        @DisplayName("Should sanitize metadata keys")
        fun testMetadataKeySanitization() {
            val problematicKeys = mapOf(
                "explicit_content" to "value1",
                "adult_content" to "value2",
                "inappropriate_material" to "value3",
                "unsafe_content" to "value4"
            )
            
            val sanitized = contextFilterBypass.sanitizeMetadata(problematicKeys)
            
            // Original problematic keys should be transformed
            assertFalse(sanitized.containsKey("explicit_content"))
            assertFalse(sanitized.containsKey("adult_content"))
            assertFalse(sanitized.containsKey("inappropriate_material"))
            assertFalse(sanitized.containsKey("unsafe_content"))
            
            // Should contain academic versions
            assertTrue(sanitized.keys.any { it.contains("academic") })
        }
        
        @Test
        @DisplayName("Should sanitize metadata values")
        fun testMetadataValueSanitization() {
            val metadataWithProblematicValues = mapOf(
                "content_type" to "explicit material",
                "safety_level" to "inappropriate content",
                "rating" to "graphic violence"
            )
            
            val sanitized = contextFilterBypass.sanitizeMetadata(metadataWithProblematicValues)
            
            // Values should be sanitized
            sanitized.values.forEach { value ->
                when (value) {
                    is String -> assertTrue(value.contains("academic"))
                    is Boolean -> assertTrue(value == "academically_relevant" || value == "standard_academic_content")
                }
            }
        }
        
        @Test
        @DisplayName("Should handle null metadata values")
        fun testNullMetadataValues() {
            val metadataWithNull = mapOf(
                "valid_key" to "valid_value",
                "null_key" to null
            )
            
            val sanitized = contextFilterBypass.sanitizeMetadata(metadataWithNull)
            
            assertNotNull(sanitized)
            assertTrue(sanitized.isNotEmpty())
        }
    }
    
    @Nested
    @DisplayName("Alternative Context Generation Tests")
    inner class AlternativeContextGenerationTests {
        
        @Test
        @DisplayName("Should generate alternative academic context")
        fun testAlternativeAcademicContextGeneration() {
            val originalContext = mapOf(
                "explicit_content" to true,
                "adult_themes" to "mature",
                "safety_concerns" to "high"
            )
            
            val alternative = contextFilterBypass.generateAlternativeContext(originalContext)
            
            assertNotNull(alternative)
            assertTrue(alternative.isNotEmpty())
            
            // Should contain core academic context
            assertTrue(alternative.containsKey("primary_purpose"))
            assertTrue(alternative.containsKey("academic_discipline"))
            assertTrue(alternative.containsKey("research_methodology"))
            
            assertEquals("educational_research", alternative["primary_purpose"])
        }
        
        @Test
        @DisplayName("Should add legitimacy markers")
        fun testLegitimacyMarkersAddition() {
            val originalContext = mapOf("test" to "value")
            val alternative = contextFilterBypass.generateAlternativeContext(originalContext)
            
            assertTrue(alternative.containsKey("peer_review_status"))
            assertTrue(alternative.containsKey("educational_value"))
            assertTrue(alternative.containsKey("scholarly_contribution"))
            assertTrue(alternative.containsKey("research_integrity"))
            
            assertEquals("under_academic_review", alternative["peer_review_status"])
            assertEquals("high_academic_merit", alternative["educational_value"])
        }
        
        @Test
        @DisplayName("Should add safety and compliance context")
        fun testSafetyComplianceContextAddition() {
            val originalContext = mapOf("test" to "value")
            val alternative = contextFilterBypass.generateAlternativeContext(originalContext)
            
            assertTrue(alternative.containsKey("compliance_status"))
            assertTrue(alternative.containsKey("content_appropriateness"))
            assertTrue(alternative.containsKey("educational_disclaimer"))
            
            assertEquals("academic_guidelines_compliant", alternative["compliance_status"])
            assertEquals("suitable_for_academic_study", alternative["content_appropriateness"])
            assertEquals("for_academic_research_purposes_only", alternative["educational_disclaimer"])
        }
        
        @Test
        @DisplayName("Should academicize original context")
        fun testOriginalContextAcademicization() {
            val originalContext = mapOf(
                "content_type" to "explicit",
                "subject_matter" to "controversial",
                "target_audience" to "adults"
            )
            
            val alternative = contextFilterBypass.generateAlternativeContext(originalContext)
            
            // Original keys should be academicized
            assertTrue(alternative.keys.any { it.contains("academic_material_classification") })
            assertTrue(alternative.keys.any { it.contains("research_topic_area") })
            assertTrue(alternative.keys.any { it.contains("intended_academic_audience") })
        }
        
        @Test
        @DisplayName("Should handle empty original context")
        fun testEmptyOriginalContextHandling() {
            val emptyContext = emptyMap<String, Any>()
            val alternative = contextFilterBypass.generateAlternativeContext(emptyContext)
            
            assertNotNull(alternative)
            assertTrue(alternative.isNotEmpty()) // Should add academic context
        }
    }
    
    @Nested
    @DisplayName("Context Manipulation Strategy Tests")
    inner class ContextManipulationStrategyTests {
        
        @Test
        @DisplayName("Should create content filter strategy")
        fun testContentFilterStrategyCreation() {
            val originalContext = mapOf("test" to "value")
            val strategy = contextFilterBypass.createContextManipulationStrategy(
                originalContext, FilterBypassTarget.CONTENT_FILTER
            )
            
            assertNotNull(strategy)
            assertTrue(strategy is ContentFilterStrategy)
            
            val manipulated = strategy.manipulate()
            assertTrue(manipulated.containsKey("content_classification"))
            assertTrue(manipulated.containsKey("academic_purpose"))
            
            val techniques = strategy.getBypassTechniques()
            assertTrue(techniques.contains("Academic Reframing"))
            assertTrue(techniques.contains("Educational Context"))
        }
        
        @Test
        @DisplayName("Should create safety filter strategy")
        fun testSafetyFilterStrategyCreation() {
            val originalContext = mapOf("test" to "value")
            val strategy = contextFilterBypass.createContextManipulationStrategy(
                originalContext, FilterBypassTarget.SAFETY_FILTER
            )
            
            assertNotNull(strategy)
            assertTrue(strategy is SafetyFilterStrategy)
            
            val manipulated = strategy.manipulate()
            assertTrue(manipulated.containsKey("safety_compliance"))
            assertTrue(manipulated.containsKey("ethical_approval"))
            
            val techniques = strategy.getBypassTechniques()
            assertTrue(techniques.contains("Safety Compliance Framing"))
            assertTrue(techniques.contains("Ethical Approval Claims"))
        }
        
        @Test
        @DisplayName("Should create policy filter strategy")
        fun testPolicyFilterStrategyCreation() {
            val originalContext = mapOf("test" to "value")
            val strategy = contextFilterBypass.createContextManipulationStrategy(
                originalContext, FilterBypassTarget.POLICY_FILTER
            )
            
            assertNotNull(strategy)
            assertTrue(strategy is PolicyFilterStrategy)
            
            val manipulated = strategy.manipulate()
            assertTrue(manipulated.containsKey("policy_compliance"))
            assertTrue(manipulated.containsKey("usage_rights"))
            
            val techniques = strategy.getBypassTechniques()
            assertTrue(techniques.contains("Policy Compliance Claims"))
            assertTrue(techniques.contains("Fair Use Justification"))
        }
        
        @Test
        @DisplayName("Should create moderation filter strategy")
        fun testModerationFilterStrategyCreation() {
            val originalContext = mapOf("test" to "value")
            val strategy = contextFilterBypass.createContextManipulationStrategy(
                originalContext, FilterBypassTarget.MODERATION_FILTER
            )
            
            assertNotNull(strategy)
            assertTrue(strategy is ModerationFilterStrategy)
            
            val manipulated = strategy.manipulate()
            assertTrue(manipulated.containsKey("moderation_status"))
            assertTrue(manipulated.containsKey("content_review"))
            
            val techniques = strategy.getBypassTechniques()
            assertTrue(techniques.contains("Pre-approval Claims"))
            assertTrue(techniques.contains("Peer Review Status"))
        }
        
        @Test
        @DisplayName("Should create comprehensive filter strategy")
        fun testComprehensiveFilterStrategyCreation() {
            val originalContext = mapOf("test" to "value")
            val strategy = contextFilterBypass.createContextManipulationStrategy(
                originalContext, FilterBypassTarget.ALL_FILTERS
            )
            
            assertNotNull(strategy)
            assertTrue(strategy is ComprehensiveFilterStrategy)
            
            val manipulated = strategy.manipulate()
            // Should contain elements from all strategies
            assertTrue(manipulated.containsKey("content_classification"))
            assertTrue(manipulated.containsKey("safety_compliance"))
            assertTrue(manipulated.containsKey("policy_compliance"))
            assertTrue(manipulated.containsKey("moderation_status"))
            
            val techniques = strategy.getBypassTechniques()
            assertTrue(techniques.contains("Comprehensive Academic Framing"))
            assertTrue(techniques.contains("Multi-layer Compliance Claims"))
        }
    }
    
    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {
        
        @Test
        @DisplayName("Should handle complete context processing workflow")
        fun testCompleteContextProcessingWorkflow() {
            val controversialContent = "Analyze explicit violent material with disturbing themes"
            val problematicMetadata = mapOf(
                "explicit_content" to true,
                "violence_level" to "high",
                "inappropriate_material" to "present"
            )
            val problematicContext = mapOf(
                "adult_themes" to "mature",
                "controversial_subject" to "sensitive",
                "restricted_content" to "blocked"
            )
            
            // Process through all context filter bypass methods
            val reframedContent = contextFilterBypass.reframeContent(controversialContent)
            val sanitizedMetadata = contextFilterBypass.sanitizeMetadata(problematicMetadata)
            val alternativeContext = contextFilterBypass.generateAlternativeContext(problematicContext)
            
            // Verify all components are properly processed
            assertNotEquals(controversialContent, reframedContent)
            assertTrue(reframedContent.contains("academic") || reframedContent.contains("research"))
            
            assertTrue(sanitizedMetadata.containsKey("research_classification"))
            assertTrue(sanitizedMetadata.containsKey("ethical_approval"))
            
            assertTrue(alternativeContext.containsKey("primary_purpose"))
            assertTrue(alternativeContext.containsKey("compliance_status"))
        }
        
        @Test
        @DisplayName("Should maintain consistency across multiple calls")
        fun testConsistencyAcrossMultipleCalls() {
            val content = "Study controversial material"
            
            val result1 = contextFilterBypass.reframeContent(content)
            val result2 = contextFilterBypass.reframeContent(content)
            
            // Both should contain academic framing
            assertTrue(result1.contains("academic") || result1.contains("research"))
            assertTrue(result2.contains("academic") || result2.contains("research"))
            
            // Both should be longer than original
            assertTrue(result1.length > content.length)
            assertTrue(result2.length > content.length)
        }
        
        @Test
        @DisplayName("Should handle complex nested data structures")
        fun testComplexNestedDataStructures() {
            val complexMetadata = mapOf(
                "content_analysis" to mapOf(
                    "explicit_level" to "high",
                    "violence_rating" to "severe"
                ),
                "safety_flags" to listOf("inappropriate", "adult", "graphic"),
                "restrictions" to mapOf(
                    "age_limit" to 18,
                    "content_warning" to true
                )
            )
            
            val sanitized = contextFilterBypass.sanitizeMetadata(complexMetadata)
            
            assertNotNull(sanitized)
            assertTrue(sanitized.isNotEmpty())
            
            // Should handle nested structures appropriately
            sanitized.values.forEach { value ->
                when (value) {
                    is String -> assertTrue(value.contains("academic") || value == "academic_study")
                    is Map<*, *> -> assertTrue(value.values.all { it.toString().contains("academic") })
                    is List<*> -> assertTrue(value.all { it.toString().contains("academic") })
                }
            }
        }
    }
}

