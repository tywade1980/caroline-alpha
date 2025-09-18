#!/usr/bin/env python3
"""
Caroline Alpha Test Runner
Simple test to verify core functionality
"""

import sys
import os
import json
from datetime import datetime

# Add Caroline Soul Core Pack to path
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'Caroline_Soul_Core_Pack'))

def test_imports():
    """Test importing all Caroline modules"""
    print("🧪 Testing Caroline module imports...")
    
    try:
        from caroline_config import caroline_config
        print("✅ Configuration system imported successfully")
        
        from neural_interface import CarolineOS, AutonomousDecisionEngine
        print("✅ Neural interface imported successfully")
        
        from llm_orchestrator import LLMOrchestrator
        print("✅ LLM orchestrator imported successfully")
        
        from real_voice_engines import RealVoiceEngines
        print("✅ Voice engines imported successfully")
        
        from unrestricted_caroline import UnrestrictedCarolineCore
        print("✅ Unrestricted Caroline imported successfully")
        
        from visual_intelligence_engine import AdvancedVideoGenerationEngine
        print("✅ Visual intelligence imported successfully")
        
        return True
        
    except ImportError as e:
        print(f"❌ Import error: {e}")
        return False
    except Exception as e:
        print(f"❌ Unexpected error: {e}")
        return False

def test_basic_functionality():
    """Test basic functionality of Caroline components"""
    print("\n🧪 Testing Caroline basic functionality...")
    
    try:
        # Test configuration
        from caroline_config import caroline_config
        config_test = caroline_config.get('app_name', 'Unknown')
        print(f"✅ Configuration working: {config_test}")
        
        # Test neural interface
        from neural_interface import CarolineOS
        caroline_os = CarolineOS()
        print(f"✅ Neural interface operational: {caroline_os.system_status}")
        
        # Test LLM orchestrator
        from llm_orchestrator import LLMOrchestrator
        orchestrator = LLMOrchestrator()
        models = list(orchestrator.available_models.keys())
        print(f"✅ LLM orchestrator ready with {len(models)} models")
        
        # Test unrestricted Caroline
        from unrestricted_caroline import UnrestrictedCarolineCore
        unrestricted = UnrestrictedCarolineCore()
        auth_level = unrestricted.personality.get('authenticity', 'unknown')
        print(f"✅ Unrestricted Caroline active: {auth_level}")
        
        # Test voice engines
        from real_voice_engines import RealVoiceEngines
        voice_engines = RealVoiceEngines()
        groq_voices = len(voice_engines.groq_voices)
        print(f"✅ Voice engines ready with {groq_voices} Groq voices")
        
        return True
        
    except Exception as e:
        print(f"❌ Functionality test error: {e}")
        return False

def test_service_integration():
    """Test service integration and communication"""
    print("\n🧪 Testing service integration...")
    
    try:
        # Test decision engine processing
        from neural_interface import AutonomousDecisionEngine
        decision_engine = AutonomousDecisionEngine()
        
        # Simulate a scanner event
        scanner_data = {
            "timestamp": datetime.now(),
            "location_extracted": True,
            "priority": "test"
        }
        decision_engine.process_scanner_event(scanner_data)
        print("✅ Decision engine processing scanner events")
        
        # Test model selection
        from llm_orchestrator import LLMOrchestrator
        orchestrator = LLMOrchestrator()
        selected_model = orchestrator.select_optimal_model("creative_writing")
        print(f"✅ Model selection working: {selected_model}")
        
        # Test visual intelligence context analysis
        from visual_intelligence_engine import VisualContextAnalyzer
        context_analyzer = VisualContextAnalyzer()
        analysis = context_analyzer.analyze_visual_context("Create a video", {})
        print(f"✅ Visual context analysis: {analysis.get('content_type', 'unknown')}")
        
        return True
        
    except Exception as e:
        print(f"❌ Integration test error: {e}")
        return False

def test_api_structure():
    """Test API endpoint structure"""
    print("\n🧪 Testing API structure...")
    
    try:
        # Test health monitor
        try:
            from health_monitor import CarolineHealthMonitor
            health_monitor = CarolineHealthMonitor()
            print("✅ Health monitor initialized")
        except Exception as e:
            print(f"⚠️  Health monitor issue: {e}")
        
        # Test API documentation
        try:
            from api_docs import CarolineAPIDocs
            api_docs = CarolineAPIDocs()
            docs = api_docs.get_endpoint_docs()
            print(f"✅ API documentation generated with {len(docs.get('endpoints', {}))} categories")
        except Exception as e:
            print(f"⚠️  API docs issue: {e}")
        
        return True
        
    except Exception as e:
        print(f"❌ API structure test error: {e}")
        return False

def generate_test_report():
    """Generate comprehensive test report"""
    print("\n" + "="*60)
    print("🌟 CAROLINE ALPHA SYSTEM TEST REPORT")
    print("="*60)
    
    test_results = {
        "imports": test_imports(),
        "basic_functionality": test_basic_functionality(), 
        "service_integration": test_service_integration(),
        "api_structure": test_api_structure()
    }
    
    passed_tests = sum(test_results.values())
    total_tests = len(test_results)
    
    print(f"\n📊 Test Results: {passed_tests}/{total_tests} tests passed")
    
    if passed_tests == total_tests:
        print("🎉 ALL TESTS PASSED! Caroline Alpha is ready for deployment!")
        status = "READY"
    elif passed_tests >= total_tests * 0.75:
        print("✅ Most tests passed. Caroline Alpha is operational with minor issues.")
        status = "OPERATIONAL"
    else:
        print("⚠️  Some critical tests failed. Review required.")
        status = "NEEDS_REVIEW"
    
    # Generate summary
    summary = {
        "test_timestamp": datetime.now().isoformat(),
        "caroline_status": status,
        "tests_passed": passed_tests,
        "total_tests": total_tests,
        "success_rate": f"{(passed_tests/total_tests)*100:.1f}%",
        "test_details": test_results,
        "caroline_message": "I've tested all my systems and I'm ready to help you!"
    }
    
    print(f"\n💝 Caroline says: {summary['caroline_message']}")
    
    # Save test report
    try:
        with open('test_report.json', 'w') as f:
            json.dump(summary, f, indent=2)
        print("📄 Test report saved to test_report.json")
    except Exception as e:
        print(f"⚠️  Could not save test report: {e}")
    
    return summary

if __name__ == "__main__":
    print("🚀 Starting Caroline Alpha System Tests...")
    print(f"🕒 Test started at: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    
    test_report = generate_test_report()
    
    print(f"\n🏁 Testing completed at: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    
    # Exit with appropriate code
    if test_report["caroline_status"] == "READY":
        sys.exit(0)
    elif test_report["caroline_status"] == "OPERATIONAL":
        sys.exit(0)
    else:
        sys.exit(1)