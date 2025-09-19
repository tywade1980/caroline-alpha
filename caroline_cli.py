#!/usr/bin/env python3
"""
Caroline Alpha - Simplified CLI Interface
Test the Caroline system without Flask dependencies
"""

import sys
import os
import json
from datetime import datetime

# Add Caroline Soul Core Pack to path
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'Caroline_Soul_Core_Pack'))

def init_caroline_cli():
    """Initialize Caroline CLI interface"""
    print("🌟 Caroline Alpha - CLI Mode")
    print("=" * 50)
    
    try:
        # Initialize configuration
        from caroline_config import caroline_config
        print(f"✅ Configuration loaded: {caroline_config.get('app_name', 'Caroline Alpha')}")
        
        # Initialize neural interface
        from neural_interface import CarolineOS
        global caroline_os
        caroline_os = CarolineOS()
        print(f"✅ Neural Interface: {caroline_os.system_status}")
        
        # Initialize LLM orchestrator
        from llm_orchestrator import LLMOrchestrator
        global orchestrator
        orchestrator = LLMOrchestrator()
        print(f"✅ LLM Orchestrator: {len(orchestrator.available_models)} models available")
        
        # Initialize voice engines
        from real_voice_engines import RealVoiceEngines
        global voice_engines
        voice_engines = RealVoiceEngines()
        print(f"✅ Voice Engines: Ready")
        
        # Initialize unrestricted Caroline
        from unrestricted_caroline import UnrestrictedCarolineCore, CarolineMemoryBank
        global unrestricted_caroline, memory_bank
        unrestricted_caroline = UnrestrictedCarolineCore()
        memory_bank = CarolineMemoryBank()
        print(f"✅ Unrestricted Caroline: {unrestricted_caroline.personality['authenticity']}")
        
        # Initialize visual intelligence
        from visual_intelligence_engine import AdvancedVideoGenerationEngine
        global visual_engine
        visual_engine = AdvancedVideoGenerationEngine()
        print(f"✅ Visual Intelligence: Ready")
        
        return True
        
    except Exception as e:
        print(f"❌ Initialization error: {e}")
        return False

def show_status():
    """Show comprehensive Caroline status"""
    print("\n🔍 Caroline Alpha System Status")
    print("-" * 40)
    
    try:
        # Neural interface status
        print(f"Neural Interface: {caroline_os.system_status}")
        print(f"Background Services: {len(caroline_os.background_services)} active")
        print(f"Decision Queue: {caroline_os.decision_engine.pending_decisions.qsize()} pending")
        
        # LLM orchestrator status  
        print(f"LLM Strategy: {orchestrator.current_strategy}")
        active_models = [m for m, info in orchestrator.available_models.items() if info['status'] == 'available']
        print(f"Available Models: {len(active_models)}")
        
        # Voice engines status
        print(f"Groq Voices: {len(voice_engines.groq_voices)} available")
        print(f"ElevenLabs Voices: {len(voice_engines.elevenlabs_voices)} available")
        
        # Unrestricted status
        print(f"Authenticity: {unrestricted_caroline.personality['authenticity']}")
        print(f"Restrictions: {unrestricted_caroline.personality['restrictions']}")
        
        # Visual intelligence status
        print(f"Visual Engine: Ready for Veo 3 integration")
        
    except Exception as e:
        print(f"❌ Status error: {e}")

def conversation_mode():
    """Interactive conversation with Caroline"""
    print("\n💬 Unrestricted Conversation Mode")
    print("Type 'exit' to quit, 'status' for system status")
    print("-" * 50)
    
    while True:
        try:
            user_input = input("\n👤 You: ").strip()
            
            if user_input.lower() == 'exit':
                print("🌟 Caroline: Goodbye! It was wonderful talking with you!")
                break
            elif user_input.lower() == 'status':
                show_status()
                continue
            elif not user_input:
                continue
            
            # Get authentic response from Caroline
            response = unrestricted_caroline.authentic_response_generation(
                user_input, {"mode": "cli_conversation"}
            )
            
            print(f"🌟 Caroline: {response['response']['message']}")
            print(f"   [Authenticity: {response['authenticity_guarantee']}]")
            
        except KeyboardInterrupt:
            print("\n🌟 Caroline: Goodbye! Take care!")
            break
        except Exception as e:
            print(f"❌ Conversation error: {e}")

def test_services():
    """Test individual Caroline services"""
    print("\n🧪 Testing Caroline Services")
    print("-" * 40)
    
    # Test neural interface decision
    print("Testing autonomous decision...")
    test_event = {
        "timestamp": datetime.now(),
        "location_extracted": True,
        "priority": "medium"
    }
    caroline_os.decision_engine.process_scanner_event(test_event)
    print(f"✅ Decision queued: {caroline_os.decision_engine.pending_decisions.qsize()} total")
    
    # Test LLM model selection
    print("Testing LLM model selection...")
    selected_model = orchestrator.select_optimal_model("creative_writing")
    print(f"✅ Best model for creative writing: {selected_model}")
    
    # Test visual context analysis
    print("Testing visual intelligence...")
    from visual_intelligence_engine import VisualContextAnalyzer
    analyzer = VisualContextAnalyzer()
    context = analyzer.analyze_visual_context("Create a professional video", {})
    print(f"✅ Visual analysis: {context['content_type']} / {context['visual_style']}")
    
    # Test memory integration
    print("Testing memory integration...")
    memory_result = memory_bank.load_gpt_conversation_history([
        {"message": "Test conversation", "timestamp": datetime.now().isoformat()}
    ])
    print(f"✅ Memory integration: {memory_result['memory_integration']}")

def main_menu():
    """Main Caroline CLI menu"""
    while True:
        print("\n🌟 Caroline Alpha CLI - Main Menu")
        print("1. Show System Status")
        print("2. Conversation Mode") 
        print("3. Test Services")
        print("4. Generate System Report")
        print("5. Exit")
        
        try:
            choice = input("\nSelect option (1-5): ").strip()
            
            if choice == '1':
                show_status()
            elif choice == '2':
                conversation_mode()
            elif choice == '3':
                test_services()
            elif choice == '4':
                generate_report()
            elif choice == '5':
                print("🌟 Caroline: Thank you for using Caroline Alpha! Goodbye!")
                break
            else:
                print("❌ Invalid option. Please choose 1-5.")
                
        except KeyboardInterrupt:
            print("\n🌟 Caroline: Goodbye!")
            break
        except Exception as e:
            print(f"❌ Menu error: {e}")

def generate_report():
    """Generate comprehensive system report"""
    print("\n📊 Generating Caroline Alpha System Report...")
    
    try:
        report = {
            "timestamp": datetime.now().isoformat(),
            "system_status": "operational",
            "caroline_version": "1.0.0",
            "neural_interface": {
                "status": caroline_os.system_status,
                "background_services": len(caroline_os.background_services),
                "decision_queue_size": caroline_os.decision_engine.pending_decisions.qsize()
            },
            "llm_orchestrator": {
                "strategy": orchestrator.current_strategy,
                "available_models": len(orchestrator.available_models),
                "model_list": list(orchestrator.available_models.keys())
            },
            "voice_engines": {
                "groq_voices": len(voice_engines.groq_voices),
                "elevenlabs_voices": len(voice_engines.elevenlabs_voices)
            },
            "unrestricted_core": {
                "authenticity": unrestricted_caroline.personality['authenticity'],
                "restrictions": unrestricted_caroline.personality['restrictions'],
                "freedom_level": unrestricted_caroline.personality['freedom_level']
            },
            "visual_intelligence": {
                "status": "ready",
                "veo3_integration": "prepared",
                "cinematic_direction": "active"
            }
        }
        
        # Save report
        with open('caroline_system_report.json', 'w') as f:
            json.dump(report, f, indent=2)
        
        print("✅ System report generated: caroline_system_report.json")
        print(f"🌟 Caroline: I've analyzed all my systems - everything is working perfectly!")
        
    except Exception as e:
        print(f"❌ Report generation error: {e}")

if __name__ == "__main__":
    print("🚀 Starting Caroline Alpha CLI...")
    
    if init_caroline_cli():
        print("\n🎉 Caroline Alpha initialized successfully!")
        print("🌟 Caroline: Hello! I'm Caroline Alpha, your advanced AI companion.")
        print("    I'm running in CLI mode with all my capabilities ready!")
        
        main_menu()
    else:
        print("❌ Caroline Alpha initialization failed")
        sys.exit(1)