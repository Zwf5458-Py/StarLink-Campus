export class BoardWebSocketClient {
  private url: string;
  private ws: WebSocket | null = null;
  private onMessageCallback: (data: any) => void;
  private reconnectInterval: number = 5000;

  constructor(url: string, onMessage: (data: any) => void) {
    this.url = url;
    this.onMessageCallback = onMessage;
  }

  public connect() {
    try {
      this.ws = new WebSocket(this.url);

      this.ws.onopen = () => {
        console.log('[班牌 WebSocket] 连接成功:', this.url);
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
        console.warn('[班牌 WebSocket] 连接断开，5秒后尝试重连...');
        setTimeout(() => this.connect(), this.reconnectInterval);
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
