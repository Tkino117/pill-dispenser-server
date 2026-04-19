# Smart Pill Dispenser — Server

Arduino 製の服薬ディスペンサーと通信し、スケジュール・在庫・服薬履歴を管理する Java アプリケーションです。TCP サーバーとしてデバイスと通信しつつ、同一プロセス内で CLI と Swing GUI を提供します（バックエンド兼フロントエンド）。

## 構成

MVC アーキテクチャで実装しています。

```
src/
├── Main.java             エントリーポイント（ポート 13131 で Controller を起動）
├── controller/
│   ├── Controller.java   Model / View / CLI を束ねる
│   └── cli/              CLI とコマンド群（pillset, stock, schedule, dispense など）
├── model/
│   ├── Model.java
│   ├── server/           Arduino との TCP 通信
│   ├── alarm/            服薬タイミングのアラーム管理
│   ├── pilltracker/      服薬状態の追跡
│   ├── stock/            薬の在庫管理
│   ├── history/          服薬履歴の記録
│   └── data/             薬セット（PillSet / PillSets）
└── view/                 Swing GUI（FormMain）
```

## 実行方法

IntelliJ IDEA プロジェクト（`SmartPillDispenser.iml`）として開き、`Main` を実行してください。起動時に TCP サーバーがポート **13131** で待ち受けを開始し、CLI スレッドと GUI ウィンドウが立ち上がります。

## 操作

- **GUI**: メインウィンドウから在庫・スケジュール・履歴を確認・編集できます。
- **CLI**: 標準入力からコマンドを投入します。`help` で一覧を表示します。
  - 例: `pillset add morning`, `stock set 1 20`, `dispense morning`, `schedule ...`

## Arduino との通信

`model/server/Server.java` が TCP 接続を受け付け、`MessageExecutor` が受信メッセージを解釈して `AlarmManager` や `PillTracker` と連携します。クライアント側の実装はリポジトリ直下の `client/` を参照してください。
