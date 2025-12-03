# Beamio Android App (`beamio.app`)
![Platform](https://img.shields.io/badge/platform-Android-blue.svg)
![Language](https://img.shields.io/badge/language-Kotlin-purple.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

The **Beamio Android App** is the official Android wrapper for the Beamio stablecoin wallet ecosystem (USDC on Base L2).  
It provides a fast, optimized WebView container for `https://beamio.app/app`, enhancing the user experience with native features such as camera access, refined user-agent detection, and Chrome debugging support.

> 🚀 **Goal:** Deliver a frictionless, WeChat-Pay-level experience for stablecoin payments, USD settlement, merchant checkout, and cross-border usage — with zero onboarding friction.

---

## 📸 Preview (Placeholder)


![Preview Placeholder](home.png)

---

## ✨ Features

- 📱 Native Android WebView shell for Beamio PWA  
- 🔒 Full support for IndexedDB, LocalStorage, and PWA behaviors  
- 📷 Camera access for QR scanning and authentication  
- 🎨 Auto light/dark mode support  
- 🧭 Custom User-Agent for reliable environment detection  
- 🧪 Chrome remote debugging (`chrome://inspect`)  
- 🧰 Future-ready architecture (proxy engine, tun2socks, VPN, Web3 integrations)

---

## 📂 Project Structure

```text
app/
  src/
    main/
      java/com/beamio/app/
        MainActivity.kt
        WebViewClientEx.kt
        WebChromeClientEx.kt
      res/
        layout/activity_main.xml
        mipmap/
      AndroidManifest.xml
  build.gradle(.kts)
README.md


## 📄 License (MIT)

Copyright (c) 2025 Beamio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in  
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL  
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER  
DEALINGS IN THE SOFTWARE.