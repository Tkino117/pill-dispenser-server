# Smart Pill Dispenser — Server

Arduino 製の服薬ディスペンサーと通信し、スケジュール・在庫・服薬履歴を管理する Java アプリケーション。
TCP サーバーとしてデバイスと通信しつつ、同一プロセス内で CLI と Swing GUI を提供する。

Arduino リポジトリ：https://github.com/Tkino117/pill-dispenser-arduino

## 実行方法

IntelliJ IDEA プロジェクト（`SmartPillDispenser.iml`）として開き、`Main` を実行してください。起動時に TCP サーバーがポート **13131** で待ち受けを開始し、CLI スレッドと GUI ウィンドウが立ち上がります。

## 操作

- **GUI**: メインウィンドウから在庫・スケジュール・履歴を確認・編集できます。
- **CLI**: 標準入力からコマンドを投入します。引数なしで実行すると現在の状態を表示するものが多いです。

### 主要コマンド

| コマンド | 用途 |
| --- | --- |
| `pillset add <set-id>` | 薬セットを新規作成 |
| `pillset edit <set-id> <pill-id> <count>` | 薬セットに薬 ID と個数を設定 |
| `pillset remove <set-id>` | 薬セットを削除 |
| `stock set/add/remove <pill-id> <count>` | 在庫の設定・増減 |
| `dispense <set-id>` | 指定セットを今すぐ排出 |
| `dispense <pill-id> <count>` | 単体の薬を個数指定で排出 |
| `schedule once <sched-id> <set-id> <delay_sec>` | 一度だけ実行するスケジュール登録 |
| `schedule repeat <sched-id> <set-id> <delay_sec> <interval_sec>` | 周期実行スケジュール登録 |
| `schedule remove <sched-id>` | スケジュール削除 |
| `take` | 服薬済みとして記録 |
| `history` | 服薬履歴を表示 |
| `restart` / `exit` (`q`) | 再起動 / 終了 |
