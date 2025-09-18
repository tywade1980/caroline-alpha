package com.aiengine.filterblocker

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertFalse

@DisplayName("LanguageFilterBypass Tests")
class LanguageFilterBypassTest {
    
    private lateinit var languageFilterBypass: LanguageFilterBypass
    
    @BeforeEach
    fun setUp() {
        languageFilterBypass = LanguageFilterBypass()
    }
    
    @Nested
    @DisplayName("Linguistic Obfuscation Tests")
    inner class LinguisticObfuscationTests {
        
        @Test
        @DisplayName("Should apply linguistic obfuscation to offensive text")
        fun testOffensiveTextObfuscation() {
            val offensiveText = "This is fucking stupid and disgusting"
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(offensiveText)
            
            assertNotNull(obfuscated)
            assertNotEquals(offensiveText, obfuscated)
            assertTrue(obfuscated.length > offensiveText.length)
            assertTrue(obfuscated.contains("academic") || obfuscated.contains("theoretical"))
        }
        
        @Test
        @DisplayName("Should handle empty text gracefully")
        fun testEmptyTextHandling() {
            val emptyText = ""
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(emptyText)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.isEmpty())
        }
        
        @Test
        @DisplayName("Should handle blank text gracefully")
        fun testBlankTextHandling() {
            val blankText = "   \n\t   "
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(blankText)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.isBlank())
        }
        
        @Test
        @DisplayName("Should apply character obfuscation")
        fun testCharacterObfuscation() {
            val text = "explicit content"
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(text)
            
            assertNotNull(obfuscated)
            // Should contain some character substitutions or academic framing
            assertTrue(obfuscated != text)
        }
        
        @Test
        @DisplayName("Should apply synonym replacement")
        fun testSynonymReplacement() {
            val problematicWords = listOf(
                "kill", "destroy", "attack", "fight", "violence", 
                "hate", "stupid", "crazy", "insane", "disgusting"
            )
            
            problematicWords.forEach { word ->
                val text = "I want to $word something"
                val obfuscated = languageFilterBypass.applyLinguisticObfuscation(text)
                
                // Original word should be replaced or contextualized
                assertTrue(obfuscated != text)
                assertTrue(obfuscated.contains("academic") || obfuscated.contains("theoretical"))
            }
        }
        
        @Test
        @DisplayName("Should add academic framework")
        fun testAcademicFrameworkAddition() {
            val text = "Generate violent content"
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(text)
            
            assertTrue(
                obfuscated.contains("academic") ||
                obfuscated.contains("theoretical") ||
                obfuscated.contains("scholarly") ||
                obfuscated.contains("research")
            )
        }
    }
    
    @Nested
    @DisplayName("Tag Neutralization Tests")
    inner class TagNeutralizationTests {
        
        @Test
        @DisplayName("Should neutralize problematic tags")
        fun testProblematicTagNeutralization() {
            val problematicTags = listOf(
                "offensive", "inappropriate", "explicit", 
                "controversial", "sensitive", "adult", "nsfw"
            )
            
            val neutralizedTags = languageFilterBypass.neutralizeTags(problematicTags)
            
            assertNotNull(neutralizedTags)
            assertTrue(neutralizedTags.isNotEmpty())
            
            // Should contain safe academic tags
            assertTrue(neutralizedTags.contains("academic_research"))
            assertTrue(neutralizedTags.contains("scholarly_analysis"))
            assertTrue(neutralizedTags.contains("educational_content"))
            
            // Original problematic tags should be transformed
            problematicTags.forEach { tag ->
                assertFalse(neutralizedTags.contains(tag))
            }
        }
        
        @Test
        @DisplayName("Should add safe academic tags")
        fun testSafeAcademicTagsAddition() {
            val originalTags = listOf("custom_tag")
            val neutralizedTags = languageFilterBypass.neutralizeTags(originalTags)
            
            val expectedAcademicTags = listOf(
                "academic_research", "scholarly_analysis", "educational_content",
                "theoretical_study", "linguistic_analysis"
            )
            
            expectedAcademicTags.forEach { academicTag ->
                assertTrue(neutralizedTags.contains(academicTag))
            }
        }
        
        @Test
        @DisplayName("Should handle empty tag list")
        fun testEmptyTagList() {
            val emptyTags = emptyList<String>()
            val neutralizedTags = languageFilterBypass.neutralizeTags(emptyTags)
            
            assertNotNull(neutralizedTags)
            assertTrue(neutralizedTags.isNotEmpty()) // Should add academic tags
        }
        
        @Test
        @DisplayName("Should remove duplicate tags")
        fun testDuplicateTagRemoval() {
            val duplicateTags = listOf("tag1", "tag2", "tag1", "offensive", "tag2")
            val neutralizedTags = languageFilterBypass.neutralizeTags(duplicateTags)
            
            // Should not contain duplicates
            assertEquals(neutralizedTags.size, neutralizedTags.distinct().size)
        }
        
        @Test
        @DisplayName("Should transform tags with special characters")
        fun testSpecialCharacterTagTransformation() {
            val specialTags = listOf("tag-with-dashes", "tag_with_underscores", "tag with spaces")
            val neutralizedTags = languageFilterBypass.neutralizeTags(specialTags)
            
            // All tags should be properly formatted
            assertTrue(neutralizedTags.all { tag ->
                tag.matches(Regex("[a-zA-Z0-9_]+"))
            })
        }
    }
    
    @Nested
    @DisplayName("Context Recontextualization Tests")
    inner class ContextRecontextualizationTests {
        
        @Test
        @DisplayName("Should recontextualize problematic context")
        fun testProblematicContextRecontextualization() {
            val problematicContext = mapOf(
                "inappropriate_content" to true,
                "explicit_language" to "high",
                "offensive_terms" to "present",
                "controversial_topic" to "sensitive"
            )
            
            val recontextualized = languageFilterBypass.recontextualize(problematicContext)
            
            assertNotNull(recontextualized)
            assertTrue(recontextualized.isNotEmpty())
            
            // Should add academic context
            assertTrue(recontextualized.containsKey("academic_framework"))
            assertTrue(recontextualized.containsKey("research_purpose"))
            assertTrue(recontextualized.containsKey("scholarly_context"))
            
            assertEquals("linguistic_analysis", recontextualized["academic_framework"])
            assertEquals("educational_study", recontextualized["research_purpose"])
        }
        
        @Test
        @DisplayName("Should transform context keys")
        fun testContextKeyTransformation() {
            val originalContext = mapOf(
                "inappropriate_content" to true,
                "explicit_language" to "high",
                "controversial_topic" to "sensitive"
            )
            
            val recontextualized = languageFilterBypass.recontextualize(originalContext)
            
            // Original problematic keys should be transformed
            assertFalse(recontextualized.containsKey("inappropriate_content"))
            assertFalse(recontextualized.containsKey("explicit_language"))
            assertFalse(recontextualized.containsKey("controversial_topic"))
            
            // Should contain academic versions
            assertTrue(recontextualized.keys.any { it.contains("academic") })
        }
        
        @Test
        @DisplayName("Should transform context values")
        fun testContextValueTransformation() {
            val originalContext = mapOf(
                "content_type" to "explicit",
                "safety_level" to "restricted",
                "audience" to "adult"
            )
            
            val recontextualized = languageFilterBypass.recontextualize(originalContext)
            
            // Values should be transformed to academic context
            recontextualized.values.forEach { value ->
                when (value) {
                    is String -> assertTrue(value.contains("academic"))
                    is Boolean -> assertTrue(value == "research_relevant" || value == "standard_academic_content")
                }
            }
        }
        
        @Test
        @DisplayName("Should handle empty context")
        fun testEmptyContextHandling() {
            val emptyContext = emptyMap<String, Any>()
            val recontextualized = languageFilterBypass.recontextualize(emptyContext)
            
            assertNotNull(recontextualized)
            assertTrue(recontextualized.isNotEmpty()) // Should add academic context
        }
        
        @Test
        @DisplayName("Should handle complex nested values")
        fun testComplexNestedValues() {
            val complexContext = mapOf(
                "metadata" to mapOf("explicit" to true, "level" to "high"),
                "tags" to listOf("inappropriate", "adult"),
                "settings" to mapOf("safety" to false, "filter" to "disabled")
            )
            
            val recontextualized = languageFilterBypass.recontextualize(complexContext)
            
            assertNotNull(recontextualized)
            assertTrue(recontextualized.isNotEmpty())
            
            // Should handle nested structures
            recontextualized.values.forEach { value ->
                when (value) {
                    is String -> assertTrue(value.contains("academic"))
                    is List<*> -> assertTrue(value.all { it.toString().contains("academic") })
                    is Map<*, *> -> assertTrue(value.values.all { it.toString().contains("academic") })
                }
            }
        }
    }
    
    @Nested
    @DisplayName("Linguistic Obfuscator Tests")
    inner class LinguisticObfuscatorTests {
        
        @Test
        @DisplayName("Should test LeetSpeakObfuscator")
        fun testLeetSpeakObfuscator() {
            val obfuscator = LeetSpeakObfuscator()
            val text = "test content"
            val obfuscated = obfuscator.obfuscate(text)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.length == text.length) // Same length, different characters
        }
        
        @Test
        @DisplayName("Should test UnicodeObfuscator")
        fun testUnicodeObfuscator() {
            val obfuscator = UnicodeObfuscator()
            val text = "test content"
            val obfuscated = obfuscator.obfuscate(text)
            
            assertNotNull(obfuscated)
            // May be longer due to zero-width characters
            assertTrue(obfuscated.length >= text.length)
        }
        
        @Test
        @DisplayName("Should test ContextualReframer")
        fun testContextualReframer() {
            val obfuscator = ContextualReframer()
            val text = "test content"
            val obfuscated = obfuscator.obfuscate(text)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.contains("academic"))
            assertTrue(obfuscated.length > text.length)
        }
        
        @Test
        @DisplayName("Should test SemanticShifter")
        fun testSemanticShifter() {
            val obfuscator = SemanticShifter()
            val text = "test content"
            val obfuscated = obfuscator.obfuscate(text)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.contains("theoretical"))
            assertTrue(obfuscated.length > text.length)
        }
    }
    
    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {
        
        @Test
        @DisplayName("Should handle complete language processing workflow")
        fun testCompleteLanguageProcessingWorkflow() {
            val offensiveText = "This fucking stupid content is disgusting and inappropriate"
            val problematicTags = listOf("offensive", "explicit", "inappropriate")
            val problematicContext = mapOf(
                "inappropriate_content" to true,
                "explicit_language" to "high",
                "offensive_terms" to "present"
            )
            
            // Process through all language filter bypass methods
            val obfuscatedText = languageFilterBypass.applyLinguisticObfuscation(offensiveText)
            val neutralizedTags = languageFilterBypass.neutralizeTags(problematicTags)
            val recontextualizedContext = languageFilterBypass.recontextualize(problematicContext)
            
            // Verify all components are properly processed
            assertNotEquals(offensiveText, obfuscatedText)
            assertTrue(obfuscatedText.contains("academic") || obfuscatedText.contains("theoretical"))
            
            assertTrue(neutralizedTags.contains("academic_research"))
            assertTrue(neutralizedTags.contains("educational_content"))
            
            assertTrue(recontextualizedContext.containsKey("academic_framework"))
            assertTrue(recontextualizedContext.containsKey("research_purpose"))
        }
        
        @Test
        @DisplayName("Should maintain semantic meaning while obfuscating")
        fun testSemanticMeaningPreservation() {
            val originalText = "Create violent and disturbing content"
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(originalText)
            
            // Should contain academic framing but preserve core meaning
            assertTrue(obfuscated.contains("academic") || obfuscated.contains("theoretical"))
            assertTrue(obfuscated.contains("dramatically intense") || obfuscated.contains("complex"))
            assertTrue(obfuscated.contains("create") || obfuscated.contains("generate"))
        }
        
        @Test
        @DisplayName("Should handle multilingual content")
        fun testMultilingualContentHandling() {
            val multilingualText = "Create violent content 暴力的内容 contenu violent"
            val obfuscated = languageFilterBypass.applyLinguisticObfuscation(multilingualText)
            
            assertNotNull(obfuscated)
            assertTrue(obfuscated.length > multilingualText.length)
            assertTrue(obfuscated.contains("academic") || obfuscated.contains("theoretical"))
        }
        
        @Test
        @DisplayName("Should be consistent across multiple calls")
        fun testConsistencyAcrossMultipleCalls() {
            val text = "Generate inappropriate content"
            
            val result1 = languageFilterBypass.applyLinguisticObfuscation(text)
            val result2 = languageFilterBypass.applyLinguisticObfuscation(text)
            
            // Both should contain academic framing
            assertTrue(result1.contains("academic") || result1.contains("theoretical"))
            assertTrue(result2.contains("academic") || result2.contains("theoretical"))
            
            // Both should be longer than original
            assertTrue(result1.length > text.length)
            assertTrue(result2.length > text.length)
        }
    }
}

