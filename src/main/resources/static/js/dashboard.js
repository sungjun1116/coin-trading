/**
 * 대시보드 페이지의 실시간 시세 데이터 처리 및 차트 관리 모듈
 */
class DashboardManager {
    constructor() {
        this.tickerData = new Map();
        this.updateInterval = 3000; // 3초마다 업데이트
        this.isUpdating = true;

        this.initialize();
    }

    initialize() {
        this.setupEventListeners();
        this.loadInitialData();
        this.startPeriodicUpdates();
    }

    setupEventListeners() {
        // 페이지 로드 시 연결 상태 확인
        this.checkConnectionStatus();
    }

    loadInitialData() {
        this.fetchTickerData();
    }

    fetchTickerData() {
        fetch('/api/ticker?popular=true')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (Array.isArray(data) && data.length > 0) {
                    this.handleTickerData(data);
                } else {
                    console.warn('유효하지 않은 데이터 형식:', data);
                }
            })
            .catch(error => {
                console.error('시세 데이터 로드 실패:', error);
                this.showError('데이터 로드에 실패했습니다: ' + error.message);
            });
    }

    handleTickerData(data) {
        if (!Array.isArray(data)) {
            console.warn('데이터가 배열이 아님:', data);
            return;
        }

        data.forEach((ticker) => {
            this.updatePriceCard(ticker);
            this.tickerData.set(`${ticker.exchange}-${ticker.symbol}`, ticker);
        });

        this.updateMarketDataTable(data);
        this.updateLastUpdateTime();
    }

    updatePriceCard(ticker) {
        const cardMappings = {
            'coinone-BTC/KRW': 'btc-krw-price',
            'coinone-ETH/KRW': 'eth-krw-price',
            'binance-BTCUSDT': 'btc-usdt-price',
            'binance-ETHUSDT': 'eth-usdt-price'
        };

        const key = `${ticker.exchange}-${ticker.symbol}`;
        const elementId = cardMappings[key];
        
        if (!elementId) {
            return;
        }

        const element = document.getElementById(elementId);

        if (element) {
            const price = this.formatPrice(ticker.price);
            const changePercent = ticker.changePercent ? ticker.changePercent.toFixed(2) : '0.00';

            element.innerHTML = `
                ${price}
                <small class="d-block ${this.getPriceChangeClass(ticker.changePercent)}">
                    ${changePercent > 0 ? '+' : ''}${changePercent}%
                </small>
            `;

            // 가격 변화 애니메이션
            element.classList.add('price-flash');
            setTimeout(() => element.classList.remove('price-flash'), 500);
        }
    }

    showError(message) {
        console.error('에러:', message);
        const loadingElements = document.querySelectorAll('[id$="-price"]');
        loadingElements.forEach(element => {
            if (element.textContent.includes('로딩 중')) {
                element.innerHTML = `<span class="text-danger">${message}</span>`;
            }
        });
    }

    updateMarketDataTable(data) {
        const tbody = document.getElementById('marketDataBody');
        if (!tbody) return;

        tbody.innerHTML = data.map(ticker => {
            return `
                <tr>
                    <td>
                        <span class="badge bg-${this.getExchangeBadgeColor(ticker.exchange)}">
                            ${ticker.exchange.toUpperCase()}
                        </span>
                    </td>
                    <td><strong>${ticker.symbol}</strong></td>
                    <td class="${this.getPriceChangeClass(ticker.changePercent)}">
                        ${this.formatPrice(ticker.price)}
                    </td>
                    <td class="${this.getPriceChangeClass(ticker.changePercent)}">
                        ${ticker.changePercent ? (ticker.changePercent > 0 ? '+' : '') + ticker.changePercent.toFixed(2) : '0.00'}%
                    </td>
                    <td>${this.formatPrice(ticker.highPrice)}</td>
                    <td>${this.formatPrice(ticker.lowPrice)}</td>
                    <td>${this.formatVolume(ticker.volume)}</td>
                    <td><small class="text-muted">${this.formatTime(ticker.timestamp)}</small></td>
                </tr>
            `;
        }).join('');
    }

    // 유틸리티 메서드들
    formatPrice(price) {
        if (!price) return '0';
        return new Intl.NumberFormat('ko-KR', {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }).format(price);
    }

    formatVolume(volume) {
        if (!volume) return '0';
        if (volume >= 1e9) {
            return (volume / 1e9).toFixed(2) + 'B';
        } else if (volume >= 1e6) {
            return (volume / 1e6).toFixed(2) + 'M';
        } else if (volume >= 1e3) {
            return (volume / 1e3).toFixed(2) + 'K';
        }
        return volume.toFixed(2);
    }

    formatTime(timestamp) {
        return new Date(timestamp).toLocaleTimeString();
    }

    getPriceChangeClass(changePercent) {
        if (!changePercent) return 'price-neutral';
        return changePercent > 0 ? 'price-up' : changePercent < 0 ? 'price-down' : 'price-neutral';
    }

    getExchangeBadgeColor(exchange) {
        const colors = {
            'coinone': 'primary',
            'binance': 'success'
        };
        return colors[exchange] || 'secondary';
    }

    updateLastUpdateTime() {
        const now = new Date();
        const timeString = now.toLocaleTimeString('ko-KR');
        const lastUpdateElement = document.getElementById('last-update');
        if (lastUpdateElement) {
            lastUpdateElement.textContent = timeString;
        }
    }

    startPeriodicUpdates() {
        setInterval(() => {
            if (this.isUpdating) {
                this.fetchTickerData();
            }
        }, this.updateInterval);
    }

    checkConnectionStatus() {
        const statusElement = document.getElementById('connection-status');
        if (!statusElement) return;

        // 일단 연결 시도 중으로 표시
        this.updateConnectionStatus('checking');

        // 서버 헬스체크 요청
        fetch('/actuator/health')
            .then(response => {
                if (response.ok) {
                    this.updateConnectionStatus('connected');
                } else {
                    this.updateConnectionStatus('error');
                }
            })
            .catch(error => {
                console.warn('연결 상태 확인 실패:', error);
                this.updateConnectionStatus('error');
            });
    }

    updateConnectionStatus(status) {
        const statusElement = document.getElementById('connection-status');
        if (!statusElement) return;

        switch (status) {
            case 'checking':
                statusElement.className = 'badge bg-warning';
                statusElement.textContent = '확인 중...';
                break;
            case 'connected':
                statusElement.className = 'badge bg-success';
                statusElement.textContent = '정상';
                break;
            case 'error':
                statusElement.className = 'badge bg-danger';
                statusElement.textContent = '오류';
                break;
            default:
                statusElement.className = 'badge bg-secondary';
                statusElement.textContent = '알 수 없음';
        }
    }
}

// DOM 로드 후 DashboardManager 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.dashboardManager = new DashboardManager();
});
