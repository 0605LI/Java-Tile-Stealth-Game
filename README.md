# Java Tile-based Stealth Game

本プロジェクトは、大学のJavaプログラミング演習で開発した2D潜入アクションゲームです。離散的なマップ構造と、連続的な物理移動モデルの共存をテーマとしています。

## 主な技術特徴 (Technical Features)

- **効率的な衝突判定 (Efficient Collision Detection):** 2次元配列によるタイルベース構造を採用し、座標からインデックスを直接計算することで、O(1)の計算量で高速な衝突判定を実現しました。
  Implemented O(1) collision detection by directly indexing the 2D map array, ensuring high performance even with frequent updates.

- **ハイブリッド移動モデル (Hybrid Motion Model):** プレイヤーのグリッド単位の移動と、速度ベクトル（Velocity Vector）を用いた敵AIの滑らかな巡回システムを統合しました。
  Integrated grid-based player movement with a smooth enemy AI patrol system powered by velocity vectors and real-time physics.

- **リアルタイム処理 (Real-time Processing):** JavaFXの `AnimationTimer` を活用し、60FPSでの描画更新とロジック計算（カウントダウン、状態管理）を同期させています。
  Utilized JavaFX `AnimationTimer` to synchronize 60FPS rendering with game logic, including precise countdown and state management.

- **ゲームメカニクス (Game Mechanics):** アイテム取得（鍵）によるフラグ管理と、それに応じたドアの解錠ロジックを実装し、パズル要素を加えています。
  Implemented state management for item collection (Keys) and conditional path unlocking (Doors).

## 操作方法 (Controls)

- **移動 (Move):** 矢印キー (Arrow Keys)
- **目標 (Goal):** 20秒以内に敵を避け、鍵を入手してゴールに到達してください。
  Avoid red enemies, get the key, and reach the golden goal within 20 seconds.
