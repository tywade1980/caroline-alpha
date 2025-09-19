#!/usr/bin/env python3
"""
Caroline Alpha Startup Script
Handles dependencies and graceful startup
"""

import sys
import subprocess
import os

def check_dependencies():
    """Check if required dependencies are installed"""
    required_packages = ['flask', 'numpy', 'opencv-python', 'pillow', 'requests']
    missing_packages = []
    
    for package in required_packages:
        try:
            if package == 'opencv-python':
                import cv2
            elif package == 'pillow':
                import PIL
            else:
                __import__(package)
        except ImportError:
            missing_packages.append(package)
    
    return missing_packages

def install_dependencies():
    """Install missing dependencies"""
    print("🔧 Installing Caroline Alpha dependencies...")
    try:
        subprocess.check_call([sys.executable, '-m', 'pip', 'install', '-r', 'requirements.txt'])
        return True
    except subprocess.CalledProcessError:
        return False

def start_caroline():
    """Start Caroline Alpha application"""
    print("🚀 Starting Caroline Alpha...")
    try:
        # Import and start the application
        from app import create_app
        app = create_app()
        
        print("🌟 Caroline Alpha is now operational!")
        print("🔗 Access Caroline at: http://localhost:5000")
        print("📊 Health check: http://localhost:5000/health")
        print("🔧 Capabilities: http://localhost:5000/capabilities")
        print("=" * 60)
        
        # Start the Flask development server
        app.run(
            host='0.0.0.0',
            port=int(os.getenv('PORT', 5000)),
            debug=os.getenv('DEBUG', 'False').lower() == 'true'
        )
        
    except ImportError as e:
        print(f"❌ Failed to import application: {e}")
        print("💡 Try installing dependencies with: pip install -r requirements.txt")
        return False
    except Exception as e:
        print(f"❌ Failed to start Caroline: {e}")
        return False

def main():
    """Main startup routine"""
    print("🤖 CAROLINE ALPHA - AI ASSISTANT STARTUP")
    print("=" * 50)
    
    # Check if we're in the right directory
    if not os.path.exists('app.py'):
        print("❌ app.py not found. Please run from the caroline-alpha directory.")
        return
    
    if not os.path.exists('Caroline_Soul_Core_Pack'):
        print("❌ Caroline_Soul_Core_Pack not found. Please check installation.")
        return
    
    # Check dependencies
    missing = check_dependencies()
    
    if missing:
        print(f"⚠️ Missing dependencies: {', '.join(missing)}")
        print("🔧 Attempting to install dependencies...")
        
        if not install_dependencies():
            print("❌ Failed to install dependencies automatically.")
            print("💡 Please run manually: pip install -r requirements.txt")
            return
        
        print("✅ Dependencies installed successfully!")
    else:
        print("✅ All dependencies are available!")
    
    # Add Caroline modules to path
    sys.path.append(os.path.join(os.path.dirname(__file__), 'Caroline_Soul_Core_Pack'))
    
    # Start Caroline
    start_caroline()

if __name__ == "__main__":
    main()