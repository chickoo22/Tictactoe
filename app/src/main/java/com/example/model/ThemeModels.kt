package com.example.model

import androidx.compose.ui.graphics.Color

enum class ColorPalette(val paletteName: String, val primary: Color, val secondary: Color, val background: Color, val surface: Color) {
    NEON_CYBER(
        "Neon Cyber",
        Color(0xFF00F2FE),
        Color(0xFFFF3366),
        Color(0xFF0B0F19),
        Color(0xFF161E2E)
    ),
    SUNSET_GLOW(
        "Sunset Glow",
        Color(0xFFFFB703),
        Color(0xFFFB8500),
        Color(0xFF1A0A2A),
        Color(0xFF2B1642)
    ),
    EMERALD_NIGHT(
        "Emerald Night",
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFF061A14),
        Color(0xFF0F2D24)
    ),
    MIDNIGHT_MINIMAL(
        "Midnight Minimal",
        Color(0xFFE2E8F0),
        Color(0xFF38BDF8),
        Color(0xFF090D16),
        Color(0xFF131C31)
    )
}

enum class BoardStyle(val styleName: String) {
    GLASS("Glassmorphism"),
    NEON_GRID("Cyber Neon"),
    SOLID_CARD("Clean Solid")
}

enum class MarkerStyle(val xSymbol: String, val oSymbol: String, val displayName: String) {
    CLASSIC("X", "O", "Classic (X / O)"),
    FIRE_WATER("🔥", "💧", "Elements (Fire / Water)"),
    SWORD_SHIELD("⚔️", "🛡️", "Battle (Sword / Shield)"),
    CAT_DOG("🐱", "🐶", "Pets (Cat / Dog)"),
    LIGHTNING_MOON("⚡", "🌙", "Cosmic (Lightning / Moon)")
}
