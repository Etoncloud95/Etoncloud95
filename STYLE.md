# BLOCK ALT UNO — guia de estilo

Uma frase: **blocos de materiais reais sobre uma mesa de madeira quente, iluminados por uma única luz dourada vinda do alto à esquerda, em mundos que se movem ao fundo.**

Tudo no jogo é desenhado por código (texturas, cenários, ícones, explosões e música). Não há imagens nem áudio baixados; o app funciona sem internet.

## Paleta-âncora

| Nome | Hex | Função |
|---|---|---|
| Nogueira | `#241912` | Fundo do app, painéis escuros |
| Painel | `#33241a` | Botões, cartões, barras |
| Bordo | `#d6ae7b` | Tabuleiro |
| Creme | `#f4e7d4` | Texto principal |
| Âmbar | `#eaa53c` | Destaque, botão principal, pontuação |
| Ouro | `#ffd36b` | Celebração (recorde, combo, poder) |

### Cores com significado (nunca trocar)

| Cor | Hex | Significa |
|---|---|---|
| Ouro | `#ffd36b` | Celebração: recorde, combo, poder liberado |
| Verde-folha | `#6cc070` | Sucesso: fase concluída, próxima fase |
| Vermelho-cereja | `#e0674b` | Perigo: derrota, sem espaço |
| Âmbar | `#eaa53c` | Ação principal |

## Luz

1. **Uma direção:** a luz vem do alto à esquerda. Todo bloco tem brilho em cima/esquerda e sombra embaixo/direita.
2. **Subtom quente:** tudo recebe uma lavagem dourada leve (camada `#light`), inclusive mundos frios.
3. **Contraste real:** cada bloco tem plano de luz, meio-tom e sombra definidos.
4. **Profundidade:** o fundo é sempre mais calmo e escuro que o tabuleiro; uma vinheta escurece as bordas e um foco de luz fica sobre o tabuleiro.

## Letras

- **Display:** condensada e pesada (`sans-serif-condensed` no Android), maiúsculas, espaçamento levemente aberto. O logotipo é preenchido com a textura de madeira do jogo e tem contorno marrom.
- **Texto:** sem serifa do sistema (Roboto no Android), 12–15 px.
- **Números:** sempre `tabular-nums`, para não "pular" quando a pontuação muda.

## Movimento

- **Toque → reação em 1 quadro:** a peça cresce ao ser tocada; todo botão tem som de clique.
- **Peças assentam:** ao encaixar, os blocos fazem um squash (≈0,78 × 1,12, ancorado embaixo, ~0,1 s) e soltam poeira para cima.
- **Impacto grande = pausa curta:** limpezas grandes congelam os efeitos por ~70 ms (hitstop) antes de explodir.
- **Nada no fundo compete com o tabuleiro:** fragmentos caem, quicam e somem; peças na bandeja só respiram de leve.
- **Telas entram com transição** (fade + leve subida, 0,22 s).

## Som

- Efeitos curtos (< 0,5 s), volume 0,3–0,5; música de fundo bem mais baixa.
- Cada efeito varia o tom em ±5% para não cansar.
- Toda ação tem som: tocar botão, pegar peça, encaixar, errar o encaixe, limpar linha, combo, subir de nível, recorde, derrota.
- Músicas: clássicas de domínio público. No menu, calmas; nos modos, com percussão leve que acelera com o nível.
