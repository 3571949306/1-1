/*
 * gomoku.js
 * 五子棋游戏逻辑和界面渲染
 * 仅依赖原生JS
 */
(function (global) {
  const BOARD_SIZE = 15;
  const EMPTY = 0;
  const BLACK = 1;
  const WHITE = 2;

  let board = [];
  let currentPlayer = BLACK;
  let gameOver = false;

  function initBoard() {
    board = [];
    for (let i = 0; i < BOARD_SIZE; i++) {
      board[i] = [];
      for (let j = 0; j < BOARD_SIZE; j++) {
        board[i][j] = EMPTY;
      }
    }
  }

  function renderBoard() {
    const boardEl = document.getElementById('gomoku-board');
    if (!boardEl) return;
    boardEl.innerHTML = '';
    for (let i = 0; i < BOARD_SIZE; i++) {
      for (let j = 0; j < BOARD_SIZE; j++) {
        const cell = document.createElement('div');
        cell.className = 'cell';
        cell.dataset.row = i;
        cell.dataset.col = j;
        if (board[i][j] !== EMPTY) {
          const piece = document.createElement('div');
          piece.className = `piece ${board[i][j] === BLACK ? 'black' : 'white'}`;
          cell.appendChild(piece);
        }
        cell.addEventListener('click', () => handleClick(i, j));
        boardEl.appendChild(cell);
      }
    }
  }

  function handleClick(row, col) {
    if (gameOver || currentPlayer !== BLACK) return;
    if (board[row][col] !== EMPTY) return;
    board[row][col] = BLACK;
    renderBoard();
    setStatus('请稍候...');
    if (checkWin(row, col, BLACK)) {
      setStatus('🎉 恭喜！你赢了！');
      gameOver = true;
      return;
    }
    if (isBoardFull()) {
      setStatus('平局！');
      gameOver = true;
      return;
    }
    currentPlayer = WHITE;
    setTimeout(() => {
      const aiMove = getAIMove();
      if (aiMove[0] >= 0) {
        board[aiMove[0]][aiMove[1]] = WHITE;
        renderBoard();
        if (checkWin(aiMove[0], aiMove[1], WHITE)) {
          setStatus('💀 AI赢了！');
          gameOver = true;
          return;
        }
      }
      currentPlayer = BLACK;
      setStatus('请落子');
    }, 300);
  }

  function setStatus(msg) {
    const statusEl = document.getElementById('gomoku-status');
    if (statusEl) statusEl.textContent = msg;
  }

  function checkWin(row, col, player) {
    const directions = [[1, 0], [0, 1], [1, 1], [1, -1]];
    for (const [dRow, dCol] of directions) {
      let count = 1;
      for (let i = 1; i < 5; i++) {
        const r = row + i * dRow;
        const c = col + i * dCol;
        if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === player) count++;
        else break;
      }
      for (let i = 1; i < 5; i++) {
        const r = row - i * dRow;
        const c = col - i * dCol;
        if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === player) count++;
        else break;
      }
      if (count >= 5) return true;
    }
    return false;
  }

  function getAIMove() {
    let bestMove = [-1, -1];
    let bestScore = -Infinity;
    for (let row = 0; row < BOARD_SIZE; row++) {
      for (let col = 0; col < BOARD_SIZE; col++) {
        if (board[row][col] === EMPTY) {
          const score = evaluatePosition(row, col, WHITE) + evaluatePosition(row, col, BLACK);
          if (score > bestScore) {
            bestScore = score;
            bestMove = [row, col];
          }
        }
      }
    }
    return bestMove;
  }

  function evaluatePosition(row, col, player) {
    let score = 0;
    const directions = [[1, 0], [0, 1], [1, 1], [1, -1]];
    for (const [dRow, dCol] of directions) {
      const count = countLine(row, col, dRow, dCol, player);
      const open = countOpenEnds(row, col, dRow, dCol, player);
      score += getScoreForLine(count, open);
    }
    return score;
  }

  function countLine(row, col, dRow, dCol, player) {
    let count = 1;
    for (let i = 1; i < 5; i++) {
      const r = row + i * dRow;
      const c = col + i * dCol;
      if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === player) count++;
      else break;
    }
    for (let i = 1; i < 5; i++) {
      const r = row - i * dRow;
      const c = col - i * dCol;
      if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === player) count++;
      else break;
    }
    return count;
  }

  function countOpenEnds(row, col, dRow, dCol, player) {
    let open = 0;
    let r = row + 5 * dRow;
    let c = col + 5 * dCol;
    if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === EMPTY) open++;
    r = row - 5 * dRow;
    c = col - 5 * dCol;
    if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] === EMPTY) open++;
    return open;
  }

  function getScoreForLine(count, open) {
    if (count >= 5) return 100000;
    if (count === 4 && open === 2) return 10000;
    if (count === 4 && open === 1) return 1000;
    if (count === 3 && open === 2) return 1000;
    if (count === 3 && open === 1) return 100;
    if (count === 2 && open === 2) return 100;
    if (count === 2 && open === 1) return 10;
    return count;
  }

  function isBoardFull() {
    for (let i = 0; i < BOARD_SIZE; i++) {
      for (let j = 0; j < BOARD_SIZE; j++) {
        if (board[i][j] === EMPTY) return false;
      }
    }
    return true;
  }

  function resetGame() {
    initBoard();
    currentPlayer = BLACK;
    gameOver = false;
    setStatus('请落子');
    renderBoard();
  }

  // 挂载到全局
  global.GomokuWidget = {
    mount: function (containerId) {
      const container = document.getElementById(containerId);
      if (!container) return;
      container.innerHTML = `
        <div class="gomoku-info">你执黑子 ● | AI执白子 ○</div>
        <div id="gomoku-board"></div>
        <div class="gomoku-status" id="gomoku-status">请落子</div>
        <button class="gomoku-btn" onclick="GomokuWidget.resetGame()">重新开始</button>
      `;
      initBoard();
      renderBoard();
    },
    resetGame: resetGame
  };
})(window);
