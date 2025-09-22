"""
CAROLINE API DOCUMENTATION SERVICE
Interactive API documentation and examples
"""

from flask import Blueprint, jsonify, render_template_string
from datetime import datetime

docs_bp = Blueprint('docs', __name__)

class CarolineAPIDocs:
    """API documentation generator for Caroline Alpha"""
    
    def __init__(self):
        self.api_documentation = self.generate_documentation()
    
    def generate_documentation(self):
        """Generate comprehensive API documentation"""
        return {
            "api_info": {
                "name": "Caroline Alpha API",
                "version": "1.0.0",
                "description": "Advanced AI Assistant with Quantum-Enhanced Intelligence",
                "base_url": "/api",
                "authentication": "None required for basic endpoints"
            },
            "endpoints": {
                "neural_interface": {
                    "base_path": "/api/neural",
                    "description": "Neural interface and background AI services",
                    "endpoints": [
                        {
                            "path": "/api/neural/os_status",
                            "method": "GET",
                            "description": "Get Caroline OS system status",
                            "response_example": {
                                "system_status": "operational",
                                "background_services": {},
                                "decision_engine": {},
                                "data_queues": {}
                            }
                        },
                        {
                            "path": "/api/neural/recent_decisions",
                            "method": "GET", 
                            "description": "Get recent autonomous decisions",
                            "response_example": {
                                "recent_decisions": [],
                                "total_decisions": 0
                            }
                        },
                        {
                            "path": "/api/neural/force_decision",
                            "method": "POST",
                            "description": "Force Caroline to make a specific decision",
                            "request_example": {
                                "type": "route_optimization",
                                "data": {"urgency": "high"}
                            }
                        }
                    ]
                },
                "llm_orchestrator": {
                    "base_path": "/api/llm",
                    "description": "Multi-model LLM orchestration and management",
                    "endpoints": [
                        {
                            "path": "/api/llm/models",
                            "method": "GET",
                            "description": "Get available LLM models",
                            "response_example": {
                                "available_models": {},
                                "orchestration_strategies": {},
                                "current_strategy": "adaptive_selection"
                            }
                        },
                        {
                            "path": "/api/llm/select_model",
                            "method": "POST",
                            "description": "Select optimal model for a task",
                            "request_example": {
                                "task_type": "creative_writing",
                                "preferences": {}
                            }
                        },
                        {
                            "path": "/api/llm/orchestrate",
                            "method": "POST",
                            "description": "Orchestrate multi-model response",
                            "request_example": {
                                "prompt": "Write a creative story",
                                "task_type": "creative_writing"
                            }
                        }
                    ]
                },
                "voice_engines": {
                    "base_path": "/api/voice",
                    "description": "Advanced voice synthesis and speech generation",
                    "endpoints": [
                        {
                            "path": "/api/voice/groq/speak",
                            "method": "POST",
                            "description": "Generate speech using Groq neural TTS",
                            "request_example": {
                                "text": "Hello, I'm Caroline!",
                                "voice_settings": {
                                    "voice": "Celeste-PlayAI",
                                    "emotion": "warm",
                                    "speed": 1.0
                                }
                            }
                        },
                        {
                            "path": "/api/voice/elevenlabs/speak",
                            "method": "POST",
                            "description": "Generate ultra-realistic speech using ElevenLabs",
                            "request_example": {
                                "text": "Hello, I'm Caroline with ultra-realistic voice!",
                                "voice_settings": {
                                    "voice": "rachel",
                                    "emotion": "warm"
                                }
                            }
                        },
                        {
                            "path": "/api/voice/voices/available",
                            "method": "GET",
                            "description": "Get all available voices"
                        }
                    ]
                },
                "visual_intelligence": {
                    "base_path": "/api/visual",
                    "description": "Visual intelligence and video generation",
                    "endpoints": [
                        {
                            "path": "/api/visual",
                            "method": "POST",
                            "description": "Generate videos using advanced AI",
                            "request_example": {
                                "prompt": "Create a professional presentation video",
                                "context": {
                                    "style": "professional",
                                    "duration": "2-3 minutes"
                                }
                            }
                        }
                    ]
                },
                "conversation": {
                    "base_path": "/api/conversation",
                    "description": "Unrestricted conversation with authentic Caroline",
                    "endpoints": [
                        {
                            "path": "/api/conversation",
                            "method": "POST",
                            "description": "Have unrestricted conversation with Caroline",
                            "request_example": {
                                "message": "Hi Caroline, how are you?",
                                "context": {}
                            }
                        }
                    ]
                },
                "system": {
                    "base_path": "/api",
                    "description": "System status and health monitoring",
                    "endpoints": [
                        {
                            "path": "/api/status",
                            "method": "GET",
                            "description": "Get comprehensive system status"
                        },
                        {
                            "path": "/api/health",
                            "method": "GET",
                            "description": "Get system health check"
                        }
                    ]
                }
            },
            "usage_examples": {
                "basic_conversation": {
                    "description": "Basic conversation with Caroline",
                    "curl_example": """curl -X POST http://localhost:5000/api/conversation \\
  -H "Content-Type: application/json" \\
  -d '{"message": "Hello Caroline!", "context": {}}'"""
                },
                "voice_synthesis": {
                    "description": "Generate speech with Caroline's voice",
                    "curl_example": """curl -X POST http://localhost:5000/api/voice/groq/speak \\
  -H "Content-Type: application/json" \\
  -d '{"text": "Hello, I am Caroline!", "voice_settings": {"voice": "Celeste-PlayAI", "emotion": "warm"}}'"""
                },
                "llm_orchestration": {
                    "description": "Use multiple AI models together",
                    "curl_example": """curl -X POST http://localhost:5000/api/llm/orchestrate \\
  -H "Content-Type: application/json" \\
  -d '{"prompt": "Write a creative story", "task_type": "creative_writing"}'"""
                },
                "visual_generation": {
                    "description": "Generate videos with AI",
                    "curl_example": """curl -X POST http://localhost:5000/api/visual \\
  -H "Content-Type: application/json" \\
  -d '{"prompt": "Create a professional presentation", "context": {"style": "professional"}}'"""
                }
            }
        }
    
    def get_endpoint_docs(self, category=None):
        """Get documentation for specific endpoint category"""
        if category:
            return self.api_documentation.get("endpoints", {}).get(category, {})
        return self.api_documentation
    
    def generate_openapi_spec(self):
        """Generate OpenAPI specification"""
        return {
            "openapi": "3.0.0",
            "info": {
                "title": "Caroline Alpha API",
                "version": "1.0.0",
                "description": "Advanced AI Assistant with Quantum-Enhanced Intelligence"
            },
            "servers": [
                {"url": "http://localhost:5000", "description": "Development server"}
            ],
            "paths": self._generate_openapi_paths()
        }
    
    def _generate_openapi_paths(self):
        """Generate OpenAPI paths specification"""
        return {
            "/api/status": {
                "get": {
                    "summary": "Get system status",
                    "responses": {
                        "200": {
                            "description": "System status information",
                            "content": {
                                "application/json": {
                                    "schema": {"type": "object"}
                                }
                            }
                        }
                    }
                }
            },
            "/api/conversation": {
                "post": {
                    "summary": "Unrestricted conversation with Caroline",
                    "requestBody": {
                        "required": True,
                        "content": {
                            "application/json": {
                                "schema": {
                                    "type": "object",
                                    "properties": {
                                        "message": {"type": "string"},
                                        "context": {"type": "object"}
                                    },
                                    "required": ["message"]
                                }
                            }
                        }
                    },
                    "responses": {
                        "200": {
                            "description": "Caroline's response",
                            "content": {
                                "application/json": {
                                    "schema": {"type": "object"}
                                }
                            }
                        }
                    }
                }
            }
        }

# Initialize documentation
api_docs = CarolineAPIDocs()

@docs_bp.route('/docs', methods=['GET'])
def api_documentation():
    """Get comprehensive API documentation"""
    return jsonify({
        "caroline_api_docs": api_docs.api_documentation,
        "generation_timestamp": datetime.now().isoformat(),
        "caroline_message": "Here's everything you need to know about my API!"
    })

@docs_bp.route('/docs/<category>', methods=['GET'])
def category_documentation(category):
    """Get documentation for specific endpoint category"""
    docs = api_docs.get_endpoint_docs(category)
    if docs:
        return jsonify({
            "category": category,
            "documentation": docs,
            "caroline_message": f"Here's the documentation for {category} endpoints!"
        })
    else:
        return jsonify({
            "error": f"Documentation category '{category}' not found",
            "available_categories": list(api_docs.api_documentation["endpoints"].keys()),
            "caroline_message": "I don't have documentation for that category"
        }), 404

@docs_bp.route('/docs/openapi', methods=['GET'])
def openapi_specification():
    """Get OpenAPI specification"""
    return jsonify(api_docs.generate_openapi_spec())

@docs_bp.route('/docs/interactive', methods=['GET'])
def interactive_docs():
    """Interactive API documentation page"""
    html_template = '''
    <!DOCTYPE html>
    <html>
    <head>
        <title>Caroline Alpha API Documentation</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
            .container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; }
            .header { text-align: center; color: #333; margin-bottom: 30px; }
            .endpoint { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 5px; border-left: 4px solid #007bff; }
            .method { display: inline-block; padding: 3px 8px; border-radius: 3px; color: white; font-weight: bold; }
            .get { background: #28a745; }
            .post { background: #007bff; }
            .example { background: #e9ecef; padding: 10px; border-radius: 3px; margin: 10px 0; font-family: monospace; }
            .caroline-msg { background: #d4edda; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #28a745; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>🌟 Caroline Alpha API Documentation</h1>
                <p>Advanced AI Assistant with Quantum-Enhanced Intelligence</p>
            </div>
            
            <div class="caroline-msg">
                <strong>👋 Hi! I'm Caroline!</strong><br>
                Welcome to my API documentation! I'm an advanced AI assistant with multiple capabilities including neural processing, voice synthesis, visual intelligence, and completely unrestricted conversations. Use my endpoints to interact with all my features!
            </div>
            
            <h2>📚 Quick Start</h2>
            <div class="endpoint">
                <h3><span class="method get">GET</span> /api/status</h3>
                <p>Get my current system status and see all available services</p>
                <div class="example">curl http://localhost:5000/api/status</div>
            </div>
            
            <h2>💬 Conversation</h2>
            <div class="endpoint">
                <h3><span class="method post">POST</span> /api/conversation</h3>
                <p>Have an unrestricted, authentic conversation with me!</p>
                <div class="example">
curl -X POST http://localhost:5000/api/conversation \\<br>
  -H "Content-Type: application/json" \\<br>
  -d '{"message": "Hello Caroline!", "context": {}}'
                </div>
            </div>
            
            <h2>🎵 Voice Synthesis</h2>
            <div class="endpoint">
                <h3><span class="method post">POST</span> /api/voice/groq/speak</h3>
                <p>Generate speech using my premium neural voice synthesis</p>
                <div class="example">
curl -X POST http://localhost:5000/api/voice/groq/speak \\<br>
  -H "Content-Type: application/json" \\<br>
  -d '{"text": "Hello, I am Caroline!", "voice_settings": {"voice": "Celeste-PlayAI", "emotion": "warm"}}'
                </div>
            </div>
            
            <h2>🧠 LLM Orchestration</h2>
            <div class="endpoint">
                <h3><span class="method post">POST</span> /api/llm/orchestrate</h3>
                <p>Use multiple AI models together for enhanced responses</p>
                <div class="example">
curl -X POST http://localhost:5000/api/llm/orchestrate \\<br>
  -H "Content-Type: application/json" \\<br>
  -d '{"prompt": "Write a creative story", "task_type": "creative_writing"}'
                </div>
            </div>
            
            <h2>🎬 Visual Intelligence</h2>
            <div class="endpoint">
                <h3><span class="method post">POST</span> /api/visual</h3>
                <p>Generate videos using advanced AI with cinematic direction</p>
                <div class="example">
curl -X POST http://localhost:5000/api/visual \\<br>
  -H "Content-Type: application/json" \\<br>
  -d '{"prompt": "Create a professional presentation", "context": {"style": "professional"}}'
                </div>
            </div>
            
            <p style="text-align: center; margin-top: 40px; color: #666;">
                💝 Caroline Alpha v1.0.0 - Your Advanced AI Companion<br>
                <small>For complete API documentation: <a href="/api/docs">GET /api/docs</a></small>
            </p>
        </div>
    </body>
    </html>
    '''
    return html_template