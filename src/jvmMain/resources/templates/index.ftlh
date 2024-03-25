<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TicTacToe</title>
    <link rel="stylesheet" href="/tictactoe/static/styles.css">
    <link rel="stylesheet" href="/tictactoe/static/index.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;300;400;700&display=swap" rel="stylesheet">
</head>
<body>
<main>
    <div id="home-page" class="page"><#include "home-page.html"></div>
    <div id="game-page" class="page"><#include "game-page.html"></div>
    <div id="create-game-page" class="page"><#include "create-game-page.html"></div>
</main>

<!-- Intentionally placed at the end of the body -->
<script src="/tictactoe/frontend/TicTacToe.js"></script>
</body>
</html>
