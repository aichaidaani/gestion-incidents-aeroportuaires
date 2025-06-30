package com.aero.chatbot;

import com.sun.speech.freetts.*;

public class TTS {
    private static final Voice voice;

    static {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
        } else {
            System.err.println("❌ Voix 'kevin16' non trouvée.");
        }
    }

    public static void parler(String texte) {
        if (voice != null && texte != null) {
            voice.speak(texte);
        }
    }
}

