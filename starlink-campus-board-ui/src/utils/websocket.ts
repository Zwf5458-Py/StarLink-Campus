export class BoardWebSocketClient {
  private url: string;
  private ws: WebSocket | null = null;
  private onMessageCallback: (data: any) => void;
  private retryCount: number = 0;

  constructor(url: string, onMessage: (data: any) => void) {
    this.url = url;
    this.onMessageCallback = onMessage;
  }

  public connect() {
    try {
      this.ws = new WebSocket(this.url);

      this.ws.onopen = () => {
        console.log('[班牌 WebSocket] 连接成功:', this.url);
        this.retryCount = 0;
      };

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          this.onMessageCallback(data);
        } catch (e) {
          console.log('[班牌 WebSocket] 接收到原始文本:', event.data);
        }
      };

      this.ws.onclose = () => {
        let backoff = Math.min(2000 * Math.pow(2, this.retryCount), 30000);
        console.warn(`[班牌 WebSocket] 连接断开，${backoff}ms 后尝试重连 (第${this.retryCount + 1}次)...`);
        this.retryCount++;
        setTimeout(() => this.connect(), backoff);
      };

      this.ws.onerror = (error) => {
        console.error('[班牌 WebSocket] 连接错误:', error);
        this.ws?.close();
      };
    } catch (e) {
      console.error('[班牌 WebSocket] 初始化失败:', e);
    }
  }

  public send(data: any) {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(data));
    }
  }
}
