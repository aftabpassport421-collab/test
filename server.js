const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const APK_PATH = path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk');
const BANNER_PATH = path.join(__dirname, 'app/src/main/res/drawable/wonder_toy_banner.jpg');
const LOGO_PATH = path.join(__dirname, 'app/src/main/res/drawable/wonder_toy_logo.jpg');

const TOYS = [
  {
    id: "toy_01",
    name: "LEGO Technic Bugatti Chiron",
    brand: "LEGO",
    category: "Building Sets",
    ageRange: "8-12 Years",
    priceQar: 1499.0,
    originalPriceQar: 1699.0,
    rating: 4.9,
    reviewCount: 142,
    badge: "Bestseller",
    description: "Classic supercar building experience with aerodynamic bodywork, spoke rims, and detailed W16 engine.",
    color: "#4F46E5",
    icon: "🏎️"
  },
  {
    id: "toy_02",
    name: "Barbie Dreamhouse Party Villa",
    brand: "Barbie",
    category: "Dolls & Playsets",
    ageRange: "5-7 Years",
    priceQar: 899.0,
    originalPriceQar: null,
    rating: 4.8,
    reviewCount: 98,
    badge: "Trending",
    description: "Features 3 stories, 10 indoor and outdoor living areas, customizable lights, pool and slide.",
    color: "#EC4899",
    icon: "🏰"
  },
  {
    id: "toy_03",
    name: "Hot Wheels Ultimate Garage Tower",
    brand: "Hot Wheels",
    category: "Vehicles & RC",
    ageRange: "5-7 Years",
    priceQar: 449.0,
    originalPriceQar: 499.0,
    rating: 4.7,
    reviewCount: 76,
    badge: "Flash Deal",
    description: "Multi-level garage with parking for over 100 cars and a robo T-Rex nemesis track challenge.",
    color: "#F97316",
    icon: "🚗"
  },
  {
    id: "toy_04",
    name: "Marvel Spider-Man Web Action Blaster",
    brand: "Marvel",
    category: "Action Figures",
    ageRange: "5-7 Years",
    priceQar: 149.0,
    originalPriceQar: null,
    rating: 4.6,
    reviewCount: 112,
    badge: null,
    description: "Wearable wrist-mounted blaster firing safe web projectiles with realistic cinematic sounds.",
    color: "#EF4444",
    icon: "🕷️"
  },
  {
    id: "toy_05",
    name: "Nerf Elite 2.0 Commander Motorized",
    brand: "Nerf",
    category: "Outdoor & Sports",
    ageRange: "8-12 Years",
    priceQar: 129.0,
    originalPriceQar: 159.0,
    rating: 4.5,
    reviewCount: 65,
    badge: "Sale",
    description: "Tactical blaster with 3 tactical rails and barrel attachment points. Includes 24 official darts.",
    color: "#3B82F6",
    icon: "🎯"
  },
  {
    id: "toy_06",
    name: "Disney Frozen Elsa Magical Ice Palace",
    brand: "Disney",
    category: "Dolls & Playsets",
    ageRange: "3-4 Years",
    priceQar: 299.0,
    originalPriceQar: null,
    rating: 4.9,
    reviewCount: 130,
    badge: "Popular",
    description: "Musical ice castle playset with flashing northern lights effect, Elsa and Olaf figures included.",
    color: "#06B6D4",
    icon: "❄️"
  },
  {
    id: "toy_07",
    name: "Play-Doh Kitchen Creations Feast",
    brand: "Play-Doh",
    category: "Arts & Crafts",
    ageRange: "3-4 Years",
    priceQar: 89.0,
    originalPriceQar: 110.0,
    rating: 4.7,
    reviewCount: 54,
    badge: "Deal",
    description: "Create colorful gourmet pretend meals, pizzas, and bakery desserts with 10 non-toxic clay cans.",
    color: "#10B981",
    icon: "🎨"
  },
  {
    id: "toy_08",
    name: "Pokémon Battle Action Pikachu Interactive",
    brand: "Pokémon",
    category: "Action Figures",
    ageRange: "5-7 Years",
    priceQar: 179.0,
    originalPriceQar: null,
    rating: 4.8,
    reviewCount: 88,
    badge: null,
    description: "Features over 20 authentic sound effects, glowing electrical cheeks, and movement sensors.",
    color: "#EAB308",
    icon: "⚡"
  },
  {
    id: "toy_09",
    name: "Fisher-Price DJ Bouncing Beat Musical",
    brand: "Fisher-Price",
    category: "Baby & Toddler",
    ageRange: "0-2 Years",
    priceQar: 219.0,
    originalPriceQar: null,
    rating: 4.9,
    reviewCount: 42,
    badge: "Top Rated",
    description: "Motorized dance buddy with 75+ learning songs, alphabet, counting and colors in English & Arabic.",
    color: "#8B5CF6",
    icon: "👶"
  },
  {
    id: "toy_10",
    name: "LEGO Star Wars Millennium Falcon",
    brand: "LEGO",
    category: "Building Sets",
    ageRange: "8-12 Years",
    priceQar: 749.0,
    originalPriceQar: 799.0,
    rating: 4.9,
    reviewCount: 167,
    badge: "Collector",
    description: "The iconic Kessel Run freighter with rotating gun turrets, spring-loaded shooters and 7 minifigures.",
    color: "#64748B",
    icon: "🚀"
  },
  {
    id: "toy_11",
    name: "Barbie Color Reveal Mermaid Fantasy",
    brand: "Barbie",
    category: "Dolls & Playsets",
    ageRange: "3-4 Years",
    priceQar: 99.0,
    originalPriceQar: null,
    rating: 4.6,
    reviewCount: 39,
    badge: null,
    description: "Dip in warm water to reveal shimmering rainbow mermaid tail and color-changing hair highlights.",
    color: "#D946EF",
    icon: "🧜‍♀️"
  },
  {
    id: "toy_12",
    name: "Hot Wheels Track Builder Unlimited Stunt Box",
    brand: "Hot Wheels",
    category: "Vehicles & RC",
    ageRange: "5-7 Years",
    priceQar: 199.0,
    originalPriceQar: 239.0,
    rating: 4.7,
    reviewCount: 61,
    badge: "Deal",
    description: "Contains 16 track pieces, curve connectors, launchers and multi-track connectors for thrilling gravity stunts.",
    color: "#F59E0B",
    icon: "🔥"
  }
];

function getHtml() {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover">
  <title>Wonder Toy Qatar 🇶🇦</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #4F46E5;
      --primary-dark: #3730A3;
      --primary-light: #EEF2FF;
      --secondary: #F97316;
      --mint: #10B981;
      --bg: #F8FAFC;
      --surface: #FFFFFF;
      --text-main: #0F172A;
      --text-muted: #64748B;
      --border: #E2E8F0;
      --radius: 16px;
    }
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      font-family: 'Plus Jakarta Sans', sans-serif;
      -webkit-tap-highlight-color: transparent;
    }
    html, body {
      width: 100%;
      min-height: 100vh;
      min-height: 100dvh;
      background: var(--bg);
      overflow-x: hidden;
    }
    body {
      display: flex;
      justify-content: center;
      align-items: stretch;
    }
    /* Mobile-First Frame that adapts perfectly to both handheld screens and desktop */
    .phone-frame {
      width: 100%;
      max-width: 100%;
      background: var(--bg);
      min-height: 100vh;
      min-height: 100dvh;
      display: flex;
      flex-direction: column;
      position: relative;
    }
    @media (min-width: 601px) {
      body {
        background: #0F172A;
        padding: 20px 0;
        align-items: flex-start;
      }
      .phone-frame {
        max-width: 440px;
        min-height: 890px;
        border-radius: 36px;
        box-shadow: 0 25px 60px -15px rgba(0, 0, 0, 0.6);
        border: 8px solid #1E293B;
        overflow: hidden;
      }
      nav {
        max-width: 424px !important;
        border-radius: 0 0 28px 28px !important;
      }
      .modal-sheet {
        max-width: 440px !important;
        border-radius: 28px 28px 0 0 !important;
      }
    }
    header {
      background: var(--surface);
      padding: 12px 16px;
      position: sticky;
      top: 0;
      z-index: 50;
      box-shadow: 0 1px 3px rgba(0,0,0,0.05);
      border-bottom: 1px solid var(--border);
    }
    .header-top {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 10px;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 10px;
    }
    .brand-logo {
      width: 42px;
      height: 42px;
      border-radius: 12px;
      object-fit: cover;
      background: var(--primary);
    }
    .brand-title {
      font-size: 19px;
      font-weight: 800;
      color: var(--primary);
      letter-spacing: -0.5px;
      display: flex;
      align-items: center;
      gap: 4px;
    }
    .brand-sub {
      font-size: 11px;
      font-weight: 600;
      color: var(--text-muted);
      display: flex;
      align-items: center;
      gap: 3px;
    }
    .header-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .icon-btn {
      position: relative;
      background: var(--primary-light);
      border: none;
      width: 40px;
      height: 40px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      font-size: 18px;
      transition: transform 0.1s;
    }
    .icon-btn:active { transform: scale(0.95); }
    .badge {
      position: absolute;
      top: -4px;
      right: -4px;
      background: var(--secondary);
      color: white;
      font-size: 10px;
      font-weight: 800;
      padding: 2px 6px;
      border-radius: 10px;
      border: 2px solid white;
    }
    .search-bar {
      position: relative;
    }
    .search-input {
      width: 100%;
      background: #F1F5F9;
      border: 1px solid transparent;
      padding: 10px 14px 10px 38px;
      border-radius: 14px;
      font-size: 13px;
      outline: none;
      transition: all 0.2s;
    }
    .search-input:focus {
      background: white;
      border-color: var(--primary);
      box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
    }
    .search-icon {
      position: absolute;
      left: 12px;
      top: 50%;
      transform: translateY(-50%);
      color: var(--text-muted);
      font-size: 14px;
    }
    .content {
      flex: 1;
      padding-bottom: calc(85px + env(safe-area-inset-bottom, 16px));
      overflow-y: auto;
      -webkit-overflow-scrolling: touch;
    }
    /* Hero Banner */
    .hero-banner {
      margin: 16px;
      border-radius: 20px;
      background: linear-gradient(135deg, #1E1B4B 0%, #312E81 50%, #4338CA 100%);
      color: white;
      padding: 20px;
      position: relative;
      overflow: hidden;
      box-shadow: 0 10px 25px -5px rgba(49, 46, 129, 0.3);
    }
    .hero-banner::after {
      content: '🧸';
      position: absolute;
      right: -10px;
      bottom: -15px;
      font-size: 110px;
      opacity: 0.15;
      transform: rotate(-15deg);
    }
    .banner-tags {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 14px;
    }
    .tag-pill {
      background: var(--secondary);
      color: white;
      font-size: 10px;
      font-weight: 800;
      padding: 4px 10px;
      border-radius: 20px;
      letter-spacing: 0.5px;
    }
    .official-pill {
      background: rgba(255,255,255,0.2);
      color: white;
      font-size: 10px;
      font-weight: 800;
      padding: 4px 10px;
      border-radius: 20px;
      display: flex;
      align-items: center;
      gap: 4px;
    }
    .banner-title {
      font-size: 20px;
      font-weight: 800;
      margin-bottom: 4px;
      line-height: 1.25;
    }
    .banner-sub {
      font-size: 12px;
      color: #E0E7FF;
      margin-bottom: 16px;
    }
    .banner-btn {
      background: var(--secondary);
      color: white;
      border: none;
      padding: 8px 18px;
      border-radius: 12px;
      font-weight: 700;
      font-size: 12px;
      cursor: pointer;
      box-shadow: 0 4px 10px rgba(249, 115, 22, 0.3);
    }
    /* Feature Card */
    .feature-card {
      margin: 0 16px 16px;
      background: white;
      padding: 12px 16px;
      border-radius: 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-shadow: 0 1px 3px rgba(0,0,0,0.05);
      border: 1px solid var(--border);
    }
    .feature-left {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .feature-icon {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background: rgba(79, 70, 229, 0.12);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
    }
    .feature-text h4 { font-size: 13px; font-weight: 700; color: var(--text-main); }
    .feature-text p { font-size: 11px; color: var(--text-muted); }
    .feature-badge {
      background: var(--mint);
      color: white;
      font-size: 10px;
      font-weight: 900;
      padding: 4px 10px;
      border-radius: 20px;
    }
    /* Section */
    .section-header {
      padding: 12px 16px 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .section-title {
      font-size: 17px;
      font-weight: 800;
      color: var(--text-main);
    }
    .section-link {
      font-size: 12px;
      font-weight: 700;
      color: var(--primary);
      cursor: pointer;
    }
    /* Horizontal scrolling chips */
    .scroll-row {
      display: flex;
      gap: 10px;
      padding: 6px 16px 14px;
      overflow-x: auto;
      scrollbar-width: none;
      -webkit-overflow-scrolling: touch;
    }
    .scroll-row::-webkit-scrollbar { display: none; }
    .chip {
      background: white;
      border: 1px solid var(--border);
      padding: 8px 14px;
      border-radius: 14px;
      font-size: 13px;
      font-weight: 700;
      color: var(--text-main);
      white-space: nowrap;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 6px;
      transition: all 0.2s;
    }
    .chip.active {
      background: var(--primary);
      color: white;
      border-color: var(--primary);
    }
    /* Toy Grid */
    .toy-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
      padding: 8px 16px;
    }
    .toy-card {
      background: white;
      border-radius: 18px;
      padding: 12px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-shadow: 0 2px 6px rgba(0,0,0,0.04);
      border: 1px solid var(--border);
      position: relative;
      cursor: pointer;
      transition: transform 0.15s;
    }
    .toy-card:hover { transform: translateY(-2px); }
    .toy-thumb {
      height: 110px;
      background: #F1F5F9;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 46px;
      margin-bottom: 10px;
      position: relative;
    }
    .toy-badge {
      position: absolute;
      top: 6px;
      left: 6px;
      background: var(--secondary);
      color: white;
      font-size: 9px;
      font-weight: 800;
      padding: 2px 6px;
      border-radius: 6px;
    }
    .fav-btn {
      position: absolute;
      top: 6px;
      right: 6px;
      background: white;
      border: none;
      width: 28px;
      height: 28px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      cursor: pointer;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .toy-brand { font-size: 10px; font-weight: 700; color: var(--primary); text-transform: uppercase; }
    .toy-name {
      font-size: 13px;
      font-weight: 700;
      color: var(--text-main);
      margin: 2px 0 6px;
      line-height: 1.3;
      height: 34px;
      overflow: hidden;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    .toy-price-row {
      display: flex;
      align-items: baseline;
      gap: 6px;
      margin-bottom: 8px;
    }
    .toy-price { font-size: 15px; font-weight: 900; color: var(--text-main); }
    .toy-old-price { font-size: 11px; color: var(--text-muted); text-decoration: line-through; }
    .toy-add-btn {
      width: 100%;
      background: var(--primary);
      color: white;
      border: none;
      padding: 8px;
      border-radius: 10px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
    }
    /* Bottom Navigation - Fixed at bottom of phone */
    nav {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      width: 100%;
      max-width: 100%;
      margin: 0 auto;
      background: white;
      border-top: 1px solid var(--border);
      display: flex;
      justify-content: space-around;
      padding: 8px 0 max(8px, env(safe-area-inset-bottom, 8px));
      z-index: 100;
      box-shadow: 0 -2px 10px rgba(0,0,0,0.03);
    }
    .nav-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 3px;
      background: none;
      border: none;
      color: var(--text-muted);
      cursor: pointer;
      font-size: 10px;
      font-weight: 600;
      position: relative;
      flex: 1;
    }
    .nav-item.active { color: var(--primary); font-weight: 800; }
    .nav-icon { font-size: 20px; }
    /* Modal / Bottom Sheet */
    .modal-overlay {
      position: fixed;
      inset: 0;
      background: rgba(15, 23, 42, 0.6);
      backdrop-filter: blur(4px);
      z-index: 200;
      display: none;
      align-items: flex-end;
      justify-content: center;
    }
    .modal-overlay.open { display: flex; }
    .modal-sheet {
      background: white;
      width: 100%;
      max-width: 100%;
      border-radius: 24px 24px 0 0;
      padding: 24px 20px max(28px, env(safe-area-inset-bottom, 24px));
      max-height: 88vh;
      overflow-y: auto;
      -webkit-overflow-scrolling: touch;
      animation: slideUp 0.25s ease-out;
    }
    @keyframes slideUp { from { transform: translateY(100%); } to { transform: translateY(0); } }
    .modal-handle {
      width: 40px;
      height: 4px;
      background: #CBD5E1;
      border-radius: 4px;
      margin: 0 auto 16px;
    }
    .btn-block {
      width: 100%;
      background: var(--primary);
      color: white;
      border: none;
      padding: 14px;
      border-radius: 14px;
      font-size: 15px;
      font-weight: 800;
      cursor: pointer;
      margin-top: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }
    .btn-secondary {
      background: var(--secondary);
    }
    .apk-banner {
      background: #1E1B4B;
      color: white;
      padding: 10px 16px;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .apk-link {
      background: var(--mint);
      color: white;
      padding: 4px 10px;
      border-radius: 8px;
      font-weight: 700;
      text-decoration: none;
      font-size: 11px;
    }
    /* Payment selector buttons */
    .pay-methods-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 8px;
      margin: 8px 0;
    }
    .pay-btn-opt {
      border: 1.5px solid var(--border);
      background: white;
      padding: 10px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      text-align: center;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      color: var(--text-main);
    }
    .pay-btn-opt.selected {
      border-color: var(--primary);
      background: var(--primary-light);
      color: var(--primary);
    }
    .card-fields-box {
      background: #F8FAFC;
      border: 1px solid var(--border);
      border-radius: 12px;
      padding: 12px;
      margin-top: 8px;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .input-field {
      width: 100%;
      padding: 10px 12px;
      border-radius: 10px;
      border: 1px solid var(--border);
      font-size: 13px;
      outline: none;
      background: white;
    }
    .input-field:focus {
      border-color: var(--primary);
    }
  </style>
</head>
<body>
  <div class="phone-frame">
    <!-- APK Notification Bar -->
    <div class="apk-banner">
      <span>📱 Wonder Toy Qatar (Direct Fast Delivery)</span>
      <a href="/download/apk" class="apk-link">Download APK</a>
    </div>

    <!-- Header -->
    <header>
      <div class="header-top">
        <div class="brand">
          <img src="/images/logo.jpg" alt="Wonder Toy" class="brand-logo" onerror="this.outerHTML='<div class=\\'brand-logo\\' style=\\'display:flex;align-items:center;justify-content:center;font-size:22px\\'>🧸</div>'">
          <div>
            <div class="brand-title">Wonder Toy 🇶🇦</div>
            <div class="brand-sub">📍 Doha, Qatar • Fast Express Delivery</div>
          </div>
        </div>
        <div class="header-actions">
          <button class="icon-btn" onclick="setTab('wishlist')">
            ❤️
            <span class="badge" id="wishlist-badge" style="display:none">0</span>
          </button>
          <button class="icon-btn" onclick="setTab('cart')">
            🛒
            <span class="badge" id="cart-badge">2</span>
          </button>
        </div>
      </div>
      <div class="search-bar">
        <span class="search-icon">🔍</span>
        <input type="text" class="search-input" id="search-input" placeholder="Search LEGO, Barbie, Marvel, Nerf..." oninput="filterToys()">
      </div>
    </header>

    <!-- Main Content Container -->
    <div class="content" id="main-content">
      <!-- Home Tab View -->
      <div id="view-home">
        <!-- Hero Banner -->
        <div class="hero-banner">
          <div class="banner-tags">
            <span class="tag-pill">⚡ DOHA SAME-DAY</span>
            <span class="official-pill">✨ 100% Original Toys</span>
          </div>
          <h2 class="banner-title">Joy Delivered Across Qatar 🇶🇦</h2>
          <p class="banner-sub">Iconic Brands • LEGO, Barbie, Marvel & Nerf</p>
          <button class="banner-btn" onclick="setTab('catalog')">Shop All Toys</button>
        </div>

        <!-- Direct Delivery Guarantee Card -->
        <div class="feature-card">
          <div class="feature-left">
            <div class="feature-icon">🚚</div>
            <div class="feature-text">
              <h4>Direct Qatar Delivery</h4>
              <p>Same-day in Doha • Doorstep arrival across Qatar</p>
            </div>
          </div>
          <span class="feature-badge">EXPRESS</span>
        </div>

        <!-- Brands Row -->
        <div class="section-header">
          <span class="section-title">Official Brands</span>
          <span class="section-link" onclick="setTab('catalog')">100% Genuine</span>
        </div>
        <div class="scroll-row" id="brands-row">
          <div class="chip active" onclick="filterBrand('All Brands')">✨ All Brands</div>
          <div class="chip" onclick="filterBrand('LEGO')">🧱 LEGO</div>
          <div class="chip" onclick="filterBrand('Barbie')">🎀 Barbie</div>
          <div class="chip" onclick="filterBrand('Hot Wheels')">🏎️ Hot Wheels</div>
          <div class="chip" onclick="filterBrand('Marvel')">⚡ Marvel</div>
          <div class="chip" onclick="filterBrand('Disney')">✨ Disney</div>
          <div class="chip" onclick="filterBrand('Nerf')">🎯 Nerf</div>
        </div>

        <!-- Age Filter -->
        <div class="section-header">
          <span class="section-title">Shop by Age</span>
        </div>
        <div class="scroll-row" id="age-row">
          <div class="chip active" onclick="filterAge('All Ages')">All Ages</div>
          <div class="chip" onclick="filterAge('0-2 Years')">0-2 Yrs</div>
          <div class="chip" onclick="filterAge('3-4 Years')">3-4 Yrs</div>
          <div class="chip" onclick="filterAge('5-7 Years')">5-7 Yrs</div>
          <div class="chip" onclick="filterAge('8-12 Years')">8-12 Yrs</div>
        </div>

        <!-- Deals & Bestsellers -->
        <div class="section-header">
          <span class="section-title">🔥 Deals & Bestsellers in Qatar</span>
          <span class="section-link" onclick="setTab('catalog')">View All</span>
        </div>
        <div class="toy-grid" id="home-toy-grid"></div>
      </div>

      <!-- Catalog Tab View -->
      <div id="view-catalog" style="display:none">
        <div class="section-header">
          <span class="section-title" id="catalog-title">All Toys</span>
          <span id="catalog-count" style="font-size:12px;color:var(--text-muted)">12 items</span>
        </div>
        <div class="scroll-row" id="category-chips">
          <div class="chip active" onclick="filterCategory('All Categories')">All</div>
          <div class="chip" onclick="filterCategory('Building Sets')">Building</div>
          <div class="chip" onclick="filterCategory('Dolls & Playsets')">Dolls</div>
          <div class="chip" onclick="filterCategory('Vehicles & RC')">Vehicles</div>
          <div class="chip" onclick="filterCategory('Action Figures')">Action</div>
          <div class="chip" onclick="filterCategory('Outdoor & Sports')">Outdoor</div>
        </div>
        <div class="toy-grid" id="catalog-toy-grid"></div>
      </div>

      <!-- Wishlist Tab View -->
      <div id="view-wishlist" style="display:none; padding:16px;">
        <div class="section-header" style="padding:0 0 12px">
          <span class="section-title">My Wishlist ❤️</span>
        </div>
        <div id="wishlist-empty" style="text-align:center; padding:40px 20px; display:none;">
          <div style="font-size:48px; margin-bottom:12px">🤍</div>
          <h3 style="font-size:16px; margin-bottom:6px">Your wishlist is empty</h3>
          <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px">Save toys you love for quick access later.</p>
          <button class="banner-btn" onclick="setTab('catalog')">Browse Toys</button>
        </div>
        <div class="toy-grid" id="wishlist-toy-grid" style="padding:0"></div>
      </div>

      <!-- Cart Tab View -->
      <div id="view-cart" style="display:none; padding:16px;">
        <div class="section-header" style="padding:0 0 12px">
          <span class="section-title">Shopping Cart 🛒</span>
        </div>
        <div id="cart-items-container"></div>
        <!-- Summary Card -->
        <div style="background:white; border-radius:16px; padding:16px; margin-top:16px; border:1px solid var(--border)">
          <div style="display:flex; justify-content:space-between; margin-bottom:8px; font-size:13px">
            <span style="color:var(--text-muted)">Subtotal</span>
            <span style="font-weight:700" id="cart-subtotal">QAR 0.00</span>
          </div>
          <div style="display:flex; justify-content:space-between; margin-bottom:8px; font-size:13px">
            <span style="color:var(--text-muted)">Promo Discount</span>
            <span style="font-weight:700; color:var(--mint)" id="cart-discount">- QAR 0.00</span>
          </div>
          <div style="display:flex; justify-content:space-between; margin-bottom:12px; font-size:13px">
            <span style="color:var(--text-muted)">Qatar Shipping</span>
            <span style="font-weight:700" id="cart-delivery">QAR 25.00</span>
          </div>
          <div style="border-top:1px dashed var(--border); padding-top:12px; display:flex; justify-content:space-between; font-size:17px; font-weight:900">
            <span>Total</span>
            <span style="color:var(--primary)" id="cart-total">QAR 0.00</span>
          </div>
          <button class="btn-block btn-secondary" onclick="openCheckout()">Proceed to Checkout 💳</button>
        </div>
      </div>

      <!-- Orders Tab View -->
      <div id="view-orders" style="display:none; padding:16px;">
        <div class="section-header" style="padding:0 0 12px">
          <span class="section-title">My Orders & Tracking 📦</span>
        </div>
        <div id="orders-container"></div>
      </div>
    </div>

    <!-- Bottom Navigation Bar (5 clean tabs, no stores) -->
    <nav>
      <button class="nav-item active" id="nav-home" onclick="setTab('home')">
        <span class="nav-icon">🏠</span>
        <span>Home</span>
      </button>
      <button class="nav-item" id="nav-catalog" onclick="setTab('catalog')">
        <span class="nav-icon">🧩</span>
        <span>Toys</span>
      </button>
      <button class="nav-item" id="nav-wishlist" onclick="setTab('wishlist')">
        <span class="nav-icon">❤️</span>
        <span>Wishlist</span>
      </button>
      <button class="nav-item" id="nav-cart" onclick="setTab('cart')">
        <span class="nav-icon">🛒</span>
        <span>Cart</span>
      </button>
      <button class="nav-item" id="nav-orders" onclick="setTab('orders')">
        <span class="nav-icon">🚚</span>
        <span>Orders</span>
      </button>
    </nav>
  </div>

  <!-- Detail Modal -->
  <div class="modal-overlay" id="detail-modal" onclick="if(event.target===this)closeDetail()">
    <div class="modal-sheet">
      <div class="modal-handle"></div>
      <div id="detail-content"></div>
    </div>
  </div>

  <!-- Checkout & Payment Modal -->
  <div class="modal-overlay" id="checkout-modal" onclick="if(event.target===this)closeCheckout()">
    <div class="modal-sheet">
      <div class="modal-handle"></div>
      <h3 style="font-size:18px; font-weight:800; margin-bottom:4px">Qatar Secure Checkout 🇶🇦</h3>
      <p style="font-size:12px; color:var(--text-muted); margin-bottom:16px">🔒 256-Bit SSL Encrypted • QCB Compliant Gateway</p>
      
      <div style="display:flex; flex-direction:column; gap:12px">
        <div>
          <label style="font-size:12px; font-weight:700; color:var(--text-muted)">Full Name</label>
          <input type="text" id="chk-name" value="Fatima Al-Kuwari" class="input-field">
        </div>
        <div>
          <label style="font-size:12px; font-weight:700; color:var(--text-muted)">Qatar Phone Number</label>
          <input type="tel" id="chk-phone" value="+974 5512 8844" class="input-field">
        </div>
        <div>
          <label style="font-size:12px; font-weight:700; color:var(--text-muted)">Delivery City</label>
          <select id="chk-city" class="input-field">
            <option>Doha</option>
            <option>Lusail</option>
            <option>The Pearl</option>
            <option>Al Wakrah</option>
            <option>Al Rayyan</option>
            <option>Al Khor</option>
          </select>
        </div>
        <div>
          <label style="font-size:12px; font-weight:700; color:var(--text-muted)">Street Address / Villa</label>
          <input type="text" id="chk-address" value="Villa 14, West Bay Lagoon" class="input-field">
        </div>

        <!-- Payment Method Selection -->
        <div>
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:4px">
            <label style="font-size:12px; font-weight:700; color:var(--text-muted)">Payment Method</label>
            <span style="font-size:10px; color:var(--primary); font-weight:700">QNB • CBQ • Visa • MC</span>
          </div>

          <div class="pay-methods-grid">
            <button type="button" class="pay-btn-opt selected" id="opt-card" onclick="selectPayMethod('card')">💳 Card</button>
            <button type="button" class="pay-btn-opt" id="opt-apple" onclick="selectPayMethod('apple')">🍏 Apple Pay</button>
            <button type="button" class="pay-btn-opt" id="opt-qpay" onclick="selectPayMethod('qpay')">🏦 QNB QPay</button>
            <button type="button" class="pay-btn-opt" id="opt-cod" onclick="selectPayMethod('cod')">💵 Cash</button>
          </div>

          <!-- Card Inputs Form -->
          <div id="card-fields-box" class="card-fields-box">
            <div style="display:flex; gap:6px; margin-bottom:4px">
              <button type="button" onclick="fillTestCard('visa')" style="flex:1; padding:6px; font-size:11px; font-weight:700; background:#EEF2FF; color:#4F46E5; border:1px solid #C7D2FE; border-radius:8px; cursor:pointer">⚡ Fill Visa Card</button>
              <button type="button" onclick="fillTestCard('naps')" style="flex:1; padding:6px; font-size:11px; font-weight:700; background:#ECFDF5; color:#059669; border:1px solid #A7F3D0; border-radius:8px; cursor:pointer">⚡ Fill QNB NAPS</button>
            </div>
            <input type="text" id="card-number" placeholder="4508 1234 5678 9124" value="4508 2384 9102 5519" class="input-field" maxlength="19">
            <div style="display:flex; gap:8px">
              <input type="text" id="card-expiry" placeholder="MM/YY" value="12/28" class="input-field" maxlength="5">
              <input type="password" id="card-cvv" placeholder="CVV" value="382" class="input-field" maxlength="4">
            </div>
            <input type="text" id="card-name" placeholder="Cardholder Name" value="MOHAMMED AL-KUWARI" class="input-field">
          </div>

          <!-- Apple Pay Info -->
          <div id="apple-pay-box" style="display:none; background:#000; color:white; border-radius:12px; padding:12px; text-align:center; margin-top:8px; font-size:13px; font-weight:700">
            🍏 1-Tap Biometric Payment with Touch ID / Face ID
          </div>

          <!-- QPay Info -->
          <div id="qpay-box" style="display:none; background:#EEF2FF; border:1px solid #C7D2FE; border-radius:12px; padding:12px; margin-top:8px; font-size:12px; color:#3730A3">
            🏦 <strong>Qatar Central Bank QPay / NAPS:</strong> Direct verification via your local Qatar bank account.
          </div>

          <!-- COD Info -->
          <div id="cod-box" style="display:none; background:#FFF7ED; border:1px solid #FFEDD5; border-radius:12px; padding:12px; margin-top:8px; font-size:12px; color:#9A3412">
            💵 <strong>Cash on Delivery:</strong> Pay in QAR to our courier at your doorstep in Doha or across Qatar.
          </div>
        </div>

        <button class="btn-block btn-secondary" onclick="confirmOrder()">Confirm & Place Order</button>
      </div>
    </div>
  </div>

  <script>
    const TOYS = ${JSON.stringify(TOYS)};
    let cart = [
      { id: "toy_01", qty: 1, giftWrap: true },
      { id: "toy_04", qty: 1, giftWrap: false }
    ];
    let wishlist = new Set(["toy_01", "toy_06"]);
    let currentTab = 'home';
    let selectedBrand = 'All Brands';
    let selectedAge = 'All Ages';
    let selectedCategory = 'All Categories';
    let searchQuery = '';
    let selectedPayment = 'card';
    let orders = [
      {
        id: "WT-90214",
        date: "Yesterday, 3:45 PM",
        item: "Disney Frozen Elsa Palace",
        total: 324.0,
        status: "DELIVERED",
        dest: "West Bay Lagoon, Doha",
        payment: "Credit Card (•••• 9124)"
      }
    ];

    function selectPayMethod(method) {
      selectedPayment = method;
      ['card', 'apple', 'qpay', 'cod'].forEach(m => {
        const btn = document.getElementById('opt-' + m);
        if (btn) btn.classList.toggle('selected', m === method);
      });
      document.getElementById('card-fields-box').style.display = method === 'card' ? 'flex' : 'none';
      document.getElementById('apple-pay-box').style.display = method === 'apple' ? 'block' : 'none';
      document.getElementById('qpay-box').style.display = method === 'qpay' ? 'block' : 'none';
      document.getElementById('cod-box').style.display = method === 'cod' ? 'block' : 'none';
    }

    function setTab(tab) {
      currentTab = tab;
      ['home', 'catalog', 'wishlist', 'cart', 'orders'].forEach(t => {
        const el = document.getElementById('view-' + t);
        if (el) el.style.display = t === tab ? 'block' : 'none';
        const navEl = document.getElementById('nav-' + t);
        if (navEl) navEl.classList.toggle('active', t === tab);
      });
      if (tab === 'home') renderHome();
      if (tab === 'catalog') renderCatalog();
      if (tab === 'wishlist') renderWishlist();
      if (tab === 'cart') renderCart();
      if (tab === 'orders') renderOrders();
      window.scrollTo(0, 0);
    }

    function createToyCard(toy) {
      const isFav = wishlist.has(toy.id);
      return \`
        <div class="toy-card" onclick="openDetail('\${toy.id}')">
          <div class="toy-thumb">
            \${toy.badge ? \`<span class="toy-badge">\${toy.badge}</span>\` : ''}
            <button class="fav-btn" onclick="event.stopPropagation(); toggleWishlist('\${toy.id}')">\${isFav ? '❤️' : '🤍'}</button>
            <span>\${toy.icon}</span>
          </div>
          <span class="toy-brand">\${toy.brand}</span>
          <h4 class="toy-name">\${toy.name}</h4>
          <div class="toy-price-row">
            <span class="toy-price">QAR \${toy.priceQar.toFixed(0)}</span>
            \${toy.originalPriceQar ? \`<span class="toy-old-price">QAR \${toy.originalPriceQar.toFixed(0)}</span>\` : ''}
          </div>
          <button class="toy-add-btn" onclick="event.stopPropagation(); addToCart('\${toy.id}')">
            🛒 Add to Cart
          </button>
        </div>
      \`;
    }

    function renderHome() {
      const bestsellers = TOYS.slice(0, 6);
      document.getElementById('home-toy-grid').innerHTML = bestsellers.map(createToyCard).join('');
    }

    function renderCatalog() {
      const filtered = TOYS.filter(t => {
        const mCat = selectedCategory === 'All Categories' || t.category === selectedCategory;
        const mBrand = selectedBrand === 'All Brands' || t.brand === selectedBrand;
        const mAge = selectedAge === 'All Ages' || t.ageRange === selectedAge;
        const mQuery = !searchQuery || t.name.toLowerCase().includes(searchQuery) || t.brand.toLowerCase().includes(searchQuery);
        return mCat && mBrand && mAge && mQuery;
      });
      document.getElementById('catalog-count').textContent = filtered.length + ' items';
      document.getElementById('catalog-toy-grid').innerHTML = filtered.map(createToyCard).join('');
    }

    function renderWishlist() {
      const items = TOYS.filter(t => wishlist.has(t.id));
      document.getElementById('wishlist-empty').style.display = items.length === 0 ? 'block' : 'none';
      document.getElementById('wishlist-toy-grid').innerHTML = items.map(createToyCard).join('');
    }

    function renderCart() {
      const container = document.getElementById('cart-items-container');
      if (cart.length === 0) {
        container.innerHTML = \`<div style="text-align:center; padding:40px 20px">
          <div style="font-size:48px; margin-bottom:12px">🛒</div>
          <h3 style="font-size:16px; margin-bottom:6px">Your Cart is Empty</h3>
          <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px">Browse our Qatar toy collection to add items.</p>
          <button class="banner-btn" onclick="setTab('catalog')">Shop Now</button>
        </div>\`;
        document.getElementById('cart-subtotal').textContent = 'QAR 0.00';
        document.getElementById('cart-discount').textContent = '- QAR 0.00';
        document.getElementById('cart-delivery').textContent = 'QAR 0.00';
        document.getElementById('cart-total').textContent = 'QAR 0.00';
        return;
      }

      let subtotal = 0;
      container.innerHTML = cart.map(c => {
        const toy = TOYS.find(t => t.id === c.id);
        const itemTotal = (toy.priceQar + (c.giftWrap ? 15 : 0)) * c.qty;
        subtotal += itemTotal;
        return \`
          <div style="background:white; border-radius:16px; padding:12px; margin-bottom:10px; border:1px solid var(--border); display:flex; gap:12px; align-items:center">
            <div style="width:60px; height:60px; background:#F1F5F9; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:28px">
              \${toy.icon}
            </div>
            <div style="flex:1">
              <h5 style="font-size:13px; font-weight:700">\${toy.name}</h5>
              <p style="font-size:12px; color:var(--primary); font-weight:800">QAR \${toy.priceQar.toFixed(0)}</p>
              <label style="font-size:11px; color:var(--text-muted); display:flex; align-items:center; gap:4px; margin-top:4px">
                <input type="checkbox" \${c.giftWrap ? 'checked' : ''} onchange="toggleGiftWrap('\${c.id}')"> 🎁 Festive Gift Wrap (+15 QAR)
              </label>
            </div>
            <div style="display:flex; align-items:center; gap:6px">
              <button onclick="changeQty('\${c.id}', -1)" style="width:26px; height:26px; border-radius:8px; border:1px solid var(--border); background:white; font-weight:bold; cursor:pointer">-</button>
              <span style="font-size:13px; font-weight:700">\${c.qty}</span>
              <button onclick="changeQty('\${c.id}', 1)" style="width:26px; height:26px; border-radius:8px; border:1px solid var(--border); background:white; font-weight:bold; cursor:pointer">+</button>
            </div>
          </div>
        \`;
      }).join('');

      const discount = subtotal * 0.10;
      const delivery = 25.0;
      const total = subtotal - discount + delivery;

      document.getElementById('cart-subtotal').textContent = 'QAR ' + subtotal.toFixed(2);
      document.getElementById('cart-discount').textContent = '- QAR ' + discount.toFixed(2) + ' (WONDER10)';
      document.getElementById('cart-delivery').textContent = 'QAR ' + delivery.toFixed(2);
      document.getElementById('cart-total').textContent = 'QAR ' + total.toFixed(2);
    }

    function renderOrders() {
      const container = document.getElementById('orders-container');
      container.innerHTML = orders.map(o => \`
        <div style="background:white; border-radius:16px; padding:16px; margin-bottom:12px; border:1px solid var(--border)">
          <div style="display:flex; justify-content:space-between; margin-bottom:8px">
            <span style="font-size:13px; font-weight:800">\${o.id}</span>
            <span style="background:rgba(16,185,129,0.15); color:var(--mint); font-size:11px; font-weight:800; padding:2px 8px; border-radius:6px">\${o.status}</span>
          </div>
          <p style="font-size:13px; font-weight:700; color:var(--text-main)">\${o.item}</p>
          <p style="font-size:11px; color:var(--text-muted); margin-top:2px">📍 \${o.dest} • \${o.date}</p>
          <p style="font-size:11px; color:var(--primary); font-weight:600; margin-top:4px">💳 \${o.payment || 'Card'}</p>
          <div style="margin-top:10px; padding-top:8px; border-top:1px dashed var(--border); display:flex; justify-content:space-between; font-size:13px">
            <span style="color:var(--text-muted)">Total Paid</span>
            <span style="font-weight:800; color:var(--primary)">QAR \${o.total.toFixed(2)}</span>
          </div>
        </div>
      \`).join('');
    }

    function addToCart(id) {
      const existing = cart.find(c => c.id === id);
      if (existing) { existing.qty += 1; }
      else { cart.push({ id, qty: 1, giftWrap: false }); }
      updateBadges();
      alert('Item added to cart!');
    }

    function toggleWishlist(id) {
      if (wishlist.has(id)) wishlist.delete(id);
      else wishlist.add(id);
      updateBadges();
      if (currentTab === 'wishlist') renderWishlist();
      else if (currentTab === 'home') renderHome();
      else if (currentTab === 'catalog') renderCatalog();
    }

    function changeQty(id, delta) {
      const item = cart.find(c => c.id === id);
      if (item) {
        item.qty += delta;
        if (item.qty <= 0) cart = cart.filter(c => c.id !== id);
      }
      updateBadges();
      renderCart();
    }

    function toggleGiftWrap(id) {
      const item = cart.find(c => c.id === id);
      if (item) item.giftWrap = !item.giftWrap;
      renderCart();
    }

    function updateBadges() {
      const cartCount = cart.reduce((sum, c) => sum + c.qty, 0);
      const cartBadge = document.getElementById('cart-badge');
      if (cartBadge) {
        cartBadge.textContent = cartCount;
        cartBadge.style.display = cartCount > 0 ? 'block' : 'none';
      }

      const wishBadge = document.getElementById('wishlist-badge');
      if (wishBadge) {
        wishBadge.textContent = wishlist.size;
        wishBadge.style.display = wishlist.size > 0 ? 'block' : 'none';
      }
    }

    function openDetail(id) {
      const toy = TOYS.find(t => t.id === id);
      const isFav = wishlist.has(toy.id);
      document.getElementById('detail-content').innerHTML = \`
        <div style="text-align:center; margin-bottom:16px">
          <div style="width:120px; height:120px; background:#F1F5F9; border-radius:20px; margin:0 auto 12px; display:flex; align-items:center; justify-content:center; font-size:60px">
            \${toy.icon}
          </div>
          <span class="toy-brand">\${toy.brand}</span>
          <h2 style="font-size:18px; font-weight:800; margin:4px 0">\${toy.name}</h2>
          <p style="font-size:12px; color:var(--text-muted)">Age: \${toy.ageRange} • ⭐ \${toy.rating} (\${toy.reviewCount} reviews)</p>
          <div style="font-size:22px; font-weight:900; color:var(--primary); margin:8px 0">QAR \${toy.priceQar.toFixed(0)}</div>
        </div>
        <p style="font-size:13px; color:var(--text-muted); line-height:1.5; margin-bottom:20px">\${toy.description}</p>
        <button class="btn-block" onclick="addToCart('\${toy.id}'); closeDetail()">Add to Cart</button>
        <button class="btn-block" style="background:#F1F5F9; color:var(--text-main); margin-top:8px" onclick="toggleWishlist('\${toy.id}'); closeDetail()">\${isFav ? 'Remove from Wishlist' : 'Add to Wishlist ❤️'}</button>
      \`;
      document.getElementById('detail-modal').classList.add('open');
    }

    function closeDetail() {
      document.getElementById('detail-modal').classList.remove('open');
    }

    function openCheckout() {
      document.getElementById('checkout-modal').classList.add('open');
    }

    function closeCheckout() {
      document.getElementById('checkout-modal').classList.remove('open');
    }

    function fillTestCard(type) {
      if (type === 'visa') {
        document.getElementById('card-number').value = '4508 2384 9102 5519';
        document.getElementById('card-expiry').value = '12/28';
        document.getElementById('card-cvv').value = '382';
        document.getElementById('card-name').value = 'MOHAMMED AL-KUWARI';
      } else {
        document.getElementById('card-number').value = '5200 8192 3840 9124';
        document.getElementById('card-expiry').value = '06/29';
        document.getElementById('card-cvv').value = '714';
        document.getElementById('card-name').value = 'QNB NAPS DEBIT';
      }
    }

    function confirmOrder() {
      const name = document.getElementById('chk-name').value;
      const phone = document.getElementById('chk-phone').value;
      const city = document.getElementById('chk-city').value;
      const addr = document.getElementById('chk-address').value;
      const orderId = 'WT-' + Math.floor(10000 + Math.random() * 90000);
      
      let subtotal = 0;
      cart.forEach(c => {
        const toy = TOYS.find(t => t.id === c.id);
        if (toy) {
          subtotal += (toy.priceQar + (c.giftWrap ? 15 : 0)) * c.qty;
        }
      });
      const discount = subtotal * 0.10;
      const delivery = cart.length > 0 ? 25.0 : 0.0;
      const totalAmount = Math.max(0, subtotal - discount + delivery);

      const rawCard = document.getElementById('card-number').value || '4508 2384 9102 5519';
      const last4 = rawCard.replace(/\s+/g, '').slice(-4) || '5519';
      
      let payName = "Card (•••• " + last4 + ")";
      if (selectedPayment === 'apple') payName = "Apple Pay  (Biometric)";
      if (selectedPayment === 'qpay') payName = "QPay (QNB NAPS Gateway)";
      if (selectedPayment === 'cod') payName = "Cash on Delivery";

      orders.unshift({
        id: orderId,
        date: "Just now",
        item: (cart.length > 0 ? cart.length : 1) + " toys ordered",
        total: totalAmount > 0 ? totalAmount : 285.0,
        status: "CONFIRMED",
        dest: addr + ", " + city,
        payment: payName
      });
      cart = [];
      updateBadges();
      closeCheckout();
      setTab('orders');
      alert('🎉 Order ' + orderId + ' Placed Successfully!\n\nPayment Method: ' + payName + '\nTotal: QAR ' + (totalAmount > 0 ? totalAmount.toFixed(2) : '285.00') + '\nDelivery: Express Doha dispatched within 30 mins.');
    }

    function filterBrand(brand) {
      selectedBrand = brand;
      setTab('catalog');
    }
    function filterAge(age) {
      selectedAge = age;
      setTab('catalog');
    }
    function filterCategory(cat) {
      selectedCategory = cat;
      renderCatalog();
    }
    function filterToys() {
      searchQuery = document.getElementById('search-input').value.toLowerCase();
      if (currentTab !== 'catalog') setTab('catalog');
      else renderCatalog();
    }

    // Initialize
    renderHome();
    updateBadges();
  </script>
</body>
</html>`;
}

const server = http.createServer((req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host}`);
  const pathname = parsedUrl.pathname;

  if (pathname === '/' || pathname === '/index.html') {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    res.end(getHtml());
    return;
  }

  if (pathname === '/download/apk') {
    if (fs.existsSync(APK_PATH)) {
      const stat = fs.statSync(APK_PATH);
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Disposition': 'attachment; filename="wondertoy-qatar.apk"',
        'Content-Length': stat.size
      });
      fs.createReadStream(APK_PATH).pipe(res);
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK building in background, please refresh in 30 seconds');
    }
    return;
  }

  if (pathname === '/images/banner.jpg') {
    if (fs.existsSync(BANNER_PATH)) {
      res.writeHead(200, { 'Content-Type': 'image/jpeg' });
      fs.createReadStream(BANNER_PATH).pipe(res);
    } else {
      res.writeHead(404);
      res.end();
    }
    return;
  }

  if (pathname === '/images/logo.jpg') {
    if (fs.existsSync(LOGO_PATH)) {
      res.writeHead(200, { 'Content-Type': 'image/jpeg' });
      fs.createReadStream(LOGO_PATH).pipe(res);
    } else {
      res.writeHead(404);
      res.end();
    }
    return;
  }

  res.writeHead(404, { 'Content-Type': 'text/plain' });
  res.end('Not Found');
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://0.0.0.0:${PORT}`);
});
