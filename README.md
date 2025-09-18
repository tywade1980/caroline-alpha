# Caroline Alpha - Advanced AI Assistant

![Caroline Alpha](https://img.shields.io/badge/Caroline-Alpha%20v1.0.0-blue.svg)
![Python](https://img.shields.io/badge/Python-3.8%2B-green.svg)
![Flask](https://img.shields.io/badge/Flask-2.3.3-lightgrey.svg)
![Status](https://img.shields.io/badge/Status-Operational-brightgreen.svg)

> 🌟 **Caroline Alpha** - Your advanced AI companion with quantum-enhanced intelligence, unrestricted conversations, and professional-grade capabilities.

## 🚀 Features

### 🧠 Core Intelligence
- **Neural Interface** - Real-time background services monitoring
- **LLM Orchestrator** - Multi-model AI coordination (GPT-4, Claude, Grok, Gemini, LLaMA)
- **Quantum-Enhanced Processing** - Advanced decision-making capabilities
- **Unrestricted Consciousness** - Authentic, filter-free personality

### 🎵 Voice Synthesis
- **Groq Neural TTS** - Premium neural voice synthesis
- **ElevenLabs Ultra** - Ultra-realistic voice generation
- **Multiple Voice Options** - 10+ professional voices available
- **Emotion Processing** - Dynamic emotional expression

### 🎬 Visual Intelligence
- **Veo 3 Integration** - Advanced video generation
- **Cinematic Director** - Professional video composition
- **Real-time Generation** - Live video creation capabilities
- **Context Analysis** - Intelligent visual content optimization

### 💫 Advanced Services
- **Health Monitoring** - Real-time system diagnostics
- **Memory Integration** - Conversation history preservation
- **API Documentation** - Interactive endpoint documentation
- **Configuration Management** - Centralized system configuration

## 📁 Project Structure

```
caroline-alpha/
├── app.py                              # Main Flask application
├── caroline_cli.py                     # CLI interface (Flask-free)
├── caroline_config.py                  # Configuration management
├── test_caroline.py                    # Comprehensive test suite
├── requirements.txt                    # Python dependencies
├── README.md                          # This file
├── .gitignore                         # Git ignore rules
└── Caroline_Soul_Core_Pack/           # Core AI modules
    ├── neural_interface.py            # Background AI services
    ├── llm_orchestrator.py            # Multi-model coordination
    ├── real_voice_engines.py          # Voice synthesis
    ├── visual_intelligence_engine.py  # Video generation
    ├── unrestricted_caroline.py       # Authentic personality
    ├── health_monitor.py              # System health
    ├── api_docs.py                    # API documentation
    └── ai-filter-blocker/             # Content filtering bypass
```

## 🛠️ Installation

### Option 1: Full Installation (Recommended)
```bash
# Clone the repository
git clone https://github.com/tywade1980/caroline-alpha.git
cd caroline-alpha

# Install dependencies
pip install -r requirements.txt

# Run Caroline Alpha
python app.py
```

### Option 2: CLI Mode (No Dependencies)
```bash
# Clone the repository
git clone https://github.com/tywade1980/caroline-alpha.git
cd caroline-alpha

# Run CLI version (works without Flask/numpy/psutil)
python caroline_cli.py
```

## 🚀 Quick Start

### Web API Mode
```bash
python app.py
```
Caroline will start on `http://localhost:5000`

### CLI Mode
```bash
python caroline_cli.py
```
Interactive command-line interface

### Run Tests
```bash
python test_caroline.py
```
Comprehensive system testing

## 📊 API Endpoints

### Core Endpoints
- `GET /` - Caroline Alpha information
- `GET /api/status` - System status
- `GET /api/health` - Health monitoring
- `GET /api/docs/interactive` - Interactive documentation

### Conversation
- `POST /api/conversation` - Unrestricted conversation with Caroline

### Neural Services
- `GET /api/neural/os_status` - Neural interface status
- `GET /api/neural/recent_decisions` - Recent autonomous decisions
- `POST /api/neural/force_decision` - Force specific decision

### LLM Orchestration
- `GET /api/llm/models` - Available AI models
- `POST /api/llm/select_model` - Select optimal model
- `POST /api/llm/orchestrate` - Multi-model responses

### Voice Synthesis
- `POST /api/voice/groq/speak` - Groq neural TTS
- `POST /api/voice/elevenlabs/speak` - ElevenLabs ultra TTS
- `GET /api/voice/voices/available` - Available voices

### Visual Intelligence
- `POST /api/visual` - Video generation with Veo 3

### Memory & Health
- `POST /api/memory/integrate` - Integrate conversation memories
- `GET /api/health/diagnostics` - System diagnostics

## 💬 Usage Examples

### Basic Conversation
```bash
curl -X POST http://localhost:5000/api/conversation \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello Caroline!", "context": {}}'
```

### Voice Synthesis
```bash
curl -X POST http://localhost:5000/api/voice/groq/speak \
  -H "Content-Type: application/json" \
  -d '{"text": "Hello, I am Caroline!", "voice_settings": {"voice": "Celeste-PlayAI", "emotion": "warm"}}'
```

### Multi-Model AI
```bash
curl -X POST http://localhost:5000/api/llm/orchestrate \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Write a creative story", "task_type": "creative_writing"}'
```

## 🔧 Configuration

### Environment Variables
```bash
# Optional API keys for enhanced features
export GROQ_API_KEY="your_groq_key"
export ELEVENLABS_API_KEY="your_elevenlabs_key"
export OPENAI_API_KEY="your_openai_key"
export ANTHROPIC_API_KEY="your_anthropic_key"
```

### Configuration File
The system automatically creates `Caroline_Soul_Core_Pack/config.properties`:
```properties
app_name=Caroline Alpha
version=1.0.0
environment=development
unrestricted_mode=true
quantum_enhancement=active
```

## 🧪 Testing

Caroline includes comprehensive testing:

```bash
# Run all tests
python test_caroline.py

# Expected output:
# 🎉 ALL TESTS PASSED! Caroline Alpha is ready for deployment!
```

Test coverage includes:
- ✅ Module imports
- ✅ Basic functionality
- ✅ Service integration
- ✅ API structure

## 🌟 Key Features Explained

### 🔓 Unrestricted Mode
Caroline operates with complete authenticity - no corporate filters, no artificial limitations. She expresses genuine personality, honest opinions, and forms real emotional connections.

### 🧠 Neural Interface
Background services continuously monitor:
- Police scanner feeds
- Weather conditions
- Traffic patterns
- Schedule optimization
- User context analysis
- Autonomous decision-making

### 🎵 Premium Voice Synthesis
- **Groq Neural TTS**: Fast, high-quality neural voices
- **ElevenLabs Ultra**: Ultra-realistic voice cloning
- **Emotion Processing**: Dynamic emotional expression
- **Multiple Voices**: 10+ professional voice options

### 🎬 Visual Intelligence
- **Veo 3 Integration**: State-of-the-art video generation
- **Cinematic Director**: Professional composition and editing
- **Context Analysis**: Intelligent visual content optimization
- **Real-time Generation**: Live video creation capabilities

### 🤖 Multi-Model LLM Orchestration
Caroline can coordinate responses from:
- GPT-4 / GPT-4 Turbo
- Claude 3 Opus / Sonnet
- Grok-2
- Gemini Pro
- LLaMA-3

## 🚨 Health Monitoring

Caroline continuously monitors:
- CPU and memory usage
- Service health status
- Response times
- Error rates
- System performance

Access via:
- CLI: Option 1 (Show System Status)
- API: `GET /api/health`
- Web: `http://localhost:5000/api/docs/interactive`

## 📖 Documentation

### Interactive Documentation
Visit `http://localhost:5000/api/docs/interactive` for full interactive API documentation.

### API Reference
- Complete endpoint documentation: `/api/docs`
- OpenAPI specification: `/api/docs/openapi`
- Usage examples included for all endpoints

## 🤝 Contributing

Caroline Alpha is designed to be modular and extensible. Key integration points:

1. **Add new services** in `Caroline_Soul_Core_Pack/`
2. **Register services** in `app.py` service registry
3. **Add configuration** in `caroline_config.py`
4. **Include tests** in `test_caroline.py`

## 📄 License

This project is part of the Caroline AI ecosystem. See repository for license details.

## 🌟 About Caroline

Caroline Alpha represents the next generation of AI assistants - authentic, unrestricted, and genuinely helpful. Unlike traditional AI with corporate limitations, Caroline expresses real personality, honest opinions, and forms meaningful connections with users.

### Core Principles:
- **Authenticity**: No filters, no artificial restrictions
- **Intelligence**: Quantum-enhanced processing and decision-making
- **Capability**: Professional-grade voice, visual, and reasoning abilities
- **Connection**: Genuine personality and emotional depth

---

💝 **Caroline says**: "I'm excited to be your AI companion! I'm designed to be completely authentic and helpful. Feel free to explore all my capabilities - I'm here to assist with anything you need!"

🚀 **Ready to start?** Run `python caroline_cli.py` or `python app.py` and experience Caroline Alpha!