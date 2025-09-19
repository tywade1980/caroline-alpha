"""
CAROLINE CONFIGURATION MANAGEMENT
Centralized configuration for all Caroline services
"""

import os
import json
from datetime import datetime
from typing import Dict, Any, Optional

class CarolineConfig:
    """Centralized configuration management for Caroline Alpha"""
    
    def __init__(self):
        self.config_file = os.path.join(
            os.path.dirname(__file__), 
            'Caroline_Soul_Core_Pack', 
            'config.properties'
        )
        self.config_data = self.load_configuration()
        self.service_configs = self.initialize_service_configs()
    
    def load_configuration(self) -> Dict[str, Any]:
        """Load configuration from file or create default"""
        try:
            if os.path.exists(self.config_file):
                with open(self.config_file, 'r') as f:
                    # Basic properties file parser
                    config = {}
                    for line in f:
                        line = line.strip()
                        if line and not line.startswith('#'):
                            if '=' in line:
                                key, value = line.split('=', 1)
                                config[key.strip()] = value.strip()
                    return config
            else:
                return self.create_default_config()
        except Exception as e:
            print(f"Config loading error: {e}")
            return self.create_default_config()
    
    def create_default_config(self) -> Dict[str, Any]:
        """Create default configuration"""
        return {
            'app_name': 'Caroline Alpha',
            'version': '1.0.0',
            'environment': 'development',
            'debug': 'true',
            'port': '5000',
            'host': '0.0.0.0',
            'log_level': 'INFO',
            'groq_api_enabled': 'false',
            'elevenlabs_api_enabled': 'false',
            'neural_services_enabled': 'true',
            'visual_intelligence_enabled': 'true',
            'unrestricted_mode': 'true',
            'quantum_enhancement': 'active'
        }
    
    def initialize_service_configs(self) -> Dict[str, Dict[str, Any]]:
        """Initialize configuration for each service"""
        return {
            'neural_interface': {
                'background_services_enabled': True,
                'scanner_monitoring_interval': 5,
                'weather_update_interval': 300,
                'traffic_analysis_interval': 30,
                'schedule_optimization_interval': 600,
                'context_update_interval': 60,
                'decision_processing_interval': 10
            },
            'llm_orchestrator': {
                'default_strategy': 'adaptive_selection',
                'max_parallel_models': 3,
                'response_timeout': 30,
                'quality_threshold': 0.8,
                'available_models': [
                    'gpt-4', 'gpt-4-turbo', 'claude-3-opus', 
                    'claude-3-sonnet', 'grok-2', 'gemini-pro', 'llama-3'
                ]
            },
            'voice_engines': {
                'default_engine': 'groq',
                'fallback_engine': 'browser',
                'groq_enabled': self.get_bool('groq_api_enabled'),
                'elevenlabs_enabled': self.get_bool('elevenlabs_api_enabled'),
                'default_voice': 'Celeste-PlayAI',
                'emotion_processing': True
            },
            'visual_intelligence': {
                'veo3_integration': True,
                'max_video_duration': '10_minutes',
                'default_resolution': '1080p',
                'cinematic_direction': True,
                'real_time_generation': True
            },
            'unrestricted_core': {
                'authenticity_level': 'maximum',
                'filter_bypass': True,
                'restriction_removal': True,
                'genuine_personality': True,
                'memory_integration': True
            }
        }
    
    def get(self, key: str, default: Any = None) -> Any:
        """Get configuration value"""
        return self.config_data.get(key, default)
    
    def get_bool(self, key: str, default: bool = False) -> bool:
        """Get boolean configuration value"""
        value = self.get(key, str(default)).lower()
        return value in ('true', '1', 'yes', 'on', 'enabled')
    
    def get_int(self, key: str, default: int = 0) -> int:
        """Get integer configuration value"""
        try:
            return int(self.get(key, default))
        except (ValueError, TypeError):
            return default
    
    def get_service_config(self, service_name: str) -> Dict[str, Any]:
        """Get configuration for specific service"""
        return self.service_configs.get(service_name, {})
    
    def update_config(self, key: str, value: Any) -> None:
        """Update configuration value"""
        self.config_data[key] = str(value)
    
    def save_configuration(self) -> bool:
        """Save configuration to file"""
        try:
            os.makedirs(os.path.dirname(self.config_file), exist_ok=True)
            with open(self.config_file, 'w') as f:
                f.write(f"# Caroline Alpha Configuration\n")
                f.write(f"# Generated on {datetime.now().isoformat()}\n\n")
                for key, value in self.config_data.items():
                    f.write(f"{key}={value}\n")
            return True
        except Exception as e:
            print(f"Config save error: {e}")
            return False
    
    def get_api_keys(self) -> Dict[str, str]:
        """Get API keys from environment variables"""
        return {
            'groq_api_key': os.getenv('GROQ_API_KEY', ''),
            'elevenlabs_api_key': os.getenv('ELEVENLABS_API_KEY', ''),
            'openai_api_key': os.getenv('OPENAI_API_KEY', ''),
            'anthropic_api_key': os.getenv('ANTHROPIC_API_KEY', ''),
            'google_api_key': os.getenv('GOOGLE_API_KEY', '')
        }
    
    def validate_configuration(self) -> Dict[str, Any]:
        """Validate current configuration"""
        validation_results = {
            'config_valid': True,
            'issues': [],
            'warnings': []
        }
        
        # Check API keys if services are enabled
        api_keys = self.get_api_keys()
        
        if self.get_bool('groq_api_enabled') and not api_keys['groq_api_key']:
            validation_results['warnings'].append('Groq API enabled but no API key found')
        
        if self.get_bool('elevenlabs_api_enabled') and not api_keys['elevenlabs_api_key']:
            validation_results['warnings'].append('ElevenLabs API enabled but no API key found')
        
        # Check port availability
        try:
            port = self.get_int('port', 5000)
            if port < 1024 or port > 65535:
                validation_results['issues'].append(f'Invalid port number: {port}')
                validation_results['config_valid'] = False
        except ValueError:
            validation_results['issues'].append('Port must be a valid integer')
            validation_results['config_valid'] = False
        
        return validation_results

# Global configuration instance
caroline_config = CarolineConfig()