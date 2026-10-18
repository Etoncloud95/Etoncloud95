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

## Estilos visuais (v11, Pixel na v12)

Uma única configuração (`Ajustes → Estilo visual`, salva como `visualStyle`) escolhe como tudo é desenhado. As regras do jogo não mudam.

| | Realismo (padrão) | Animação | Pixel (v12) |
|---|---|---|---|
| Blocos | Texturas naturais + chanfro iluminado (alto/esquerda), borda escura fina, brilho no canto | Cores chapadas com sombreamento em camadas, contorno marrom grosso, brilho estilizado e um motivo por material | Grade 16×16 em 5 tons por material, chanfro de 1 px, pontilhado só nas bordas de luz e sombra, escala sem suavização |
| Casas vazias | Furos rebaixados (sombra interna) | Casas creme arredondadas com borda suave | Casas quadradas com chanfro duro de 3 px |
| Encaixe | Squash sutil (0,93 × 1,06) | Squash elástico (0,7 × 1,3 → 1,14 × 0,86) | Squash em 3 passos (`steps`) |
| Eliminação | Bloco "racha" (clarão → encolhe); estilhaços com textura, raios de luz, brilho | Bloco "estoura" (cresce, gira, some); pedaços com contorno, estrelas, anel de impacto | Bloco "esfarela" em 4 passos; quadradinhos caem com gravidade (lava brilha e solta fumaça, gelo/metal faíscam, água espirra), anel quadrado |
| Textos | Folha de ouro com contorno escuro | Contorno cartoon com sombra dura | Fonte monoespaçada com sombra dura de pixel |

Os três estilos usam o mesmo código de jogo, as mesmas partículas e o mesmo agendamento. Só mudam os parâmetros e os pintores de textura (`Skins`), a explosão (`Scene.explode`) e as animações CSS (`body.vs-real` / `body.vs-anim` / `body.vs-pixel`).

Realismo (v13): madeira, cimento, tijolo, gelo e água voltaram às texturas da v10 (as mais naturais), agora geradas em 256 px para telas Full HD. Lava, mármore e metal seguem os pintores da v11/v12.

Fragmentos do Realismo (v13): cada bloco quebra em 4 pedaços sólidos e serrilhados (um por canto), com face texturizada e paredes laterais para baixo/direita (longe da luz). Eles giram só no plano, sem "virar" como papel; a face brilha por um instante ao girar e as bordas voltadas para a luz ficam claras. Junto: pedrinhas facetadas, faíscas com rastro, clarão no ponto do impacto, raios de luz e poeira. As animações de encaixe não mudaram.

## Paleta dos blocos (v13)

15 conjuntos de 6 cores que cobrem o círculo cromático inteiro: madeiras, arco-íris, floresta, oceano, fogo e lava, pôr do sol, joias, gelo glacial, tropical, realeza, outono, doce, cosmos, terra, pedra e aço. Os tons de madeira, gelo, fogo e pedra foram tirados das fotos de referência. Dentro de cada conjunto as cores variam em matiz e em claridade, para os blocos não se confundirem. O Clássico troca de conjunto a cada nível; cada mundo da História alterna dois conjuntos.

## Cor de cada modo

| Modo | Cor-assinatura |
|---|---|
| Clássico | Âmbar `#eaa53c` |
| Sudoku | Verde-azulado `#3fb59a` (divisórias 3×3 desenhadas) |
| Tetris | Roxo `#a66be0` |
| História | Cobre `#d9824a` |

A cor do modo aparece no nome do modo, na barra de progresso, no filete da barra superior e no contorno do tabuleiro.

## Leitura do tabuleiro

- Prévia válida: a peça na sua cor com borda tracejada; linhas que vão sumir pulsam em ouro.
- Prévia inválida: casas livres em vermelho, blocos no caminho com contorno vermelho; a peça arrastada perde a cor.
- Tetris: a peça fantasma é contornada na cor da peça.

## Combos

×2 texto · ×3–4 texto maior + anel · ×5+ "Combo épico" + anel + confete + clarão + hitstop.

## História: recompensas

- 3 estrelas por fase: concluir · desafio especial da fase · não usar o poder. Estrelas só somam (repetir nunca duplica nem remove).
- Medalha por mundo (bronze/prata/ouro) ao concluir as 10 fases, conforme as estrelas.
- Texturas desbloqueadas por estrelas: Mármore (15) e Metal (40).

## Fundo segue o estilo (v14)

O cenário animado de fundo acompanha o estilo dos blocos:
- **Realismo:** pintado em alta resolução, com degradês e brilhos suaves.
- **Animação:** o mesmo cenário em faixas de cor chapadas, como desenho animado (filtro SVG `#bau-toon`, 9 tons por canal, saturação maior).
- **Pixel:** o cenário é pintado em ¼ da resolução, ampliado com pixels duros (`image-rendering: pixelated`) e com a paleta reduzida (filtro `#bau-pix`).

## Materiais (v14)

15 materiais, todos nos três estilos, cada um com textura, fragmentos e explosão próprios:
Madeira, Cimento, Tijolo, Gelo, Água, Lava, **Raio**, **Eletricidade**, **Chuva**, **Futuro**, **Areia**, **Grama**, **Fumaça**, Mármore (15 ★) e Metal (40 ★).

| Material | Realismo | Explosão |
|---|---|---|
| Raio | Nuvens de tempestade cortadas por um raio ramificado com brilho | Arcos elétricos, faíscas, clarão branco |
| Eletricidade | Filamentos de plasma (ruído "ridged") com núcleo branco | Arcos azuis e violeta, clarão azul |
| Chuva | Vidro molhado: gotas que refratam, escorridos | Pedaços de vidro, gotas, névoa |
| Futuro | Painel escuro com circuitos neon e malha hexagonal | Pixels neon, linhas, faíscas azuis |
| Areia | Ondulações do vento, grãos finos | Torrões de arenito, grãos e poeira |
| Grama | Centenas de folhas, raiz escura e ponta iluminada | Torrões de grama, folhas rodopiando |
| Fumaça | Volutas de fumaça (ruído distorcido) | Sem pedaços sólidos: nuvens que sobem |

## Cenário e blocos andam juntos (v15)

Cada material tem o seu cenário e cada cenário tem o seu material:

| Material | Cenário |
|---|---|
| Madeira | Superfície |
| Grama | Campos Verdes |
| Mármore | Céu |
| Chuva | Floresta da Chuva |
| Raio | Tempestade |
| Areia | Deserto Dourado |
| Cimento | Lua |
| Metal | Espaço Profundo |
| Eletricidade | Usina Elétrica |
| Futuro | Cidade do Futuro |
| Lava | Vulcão |
| Fumaça | Vale da Fumaça |
| Água | Mar Aberto |
| Gelo | Abismo Oceânico |
| Tijolo | Campo de Batalha |

Nebulosa usa Eletricidade e Vale Assombrado usa Cimento.

- **Escolher uma textura no menu:** o fundo do menu passa para o cenário dela, e as partidas salvas de Clássico, Sudoku e Tetris mudam para esse material e cenário.
- **Cenário que muda com os pontos** (Clássico, Sudoku e Tetris): os blocos passam a ser do material do novo cenário. O sorteio só usa cenários cujo material já está desbloqueado.

## Tabuleiro (v16)

O tabuleiro (bandeja onde os blocos são colocados) é feito do material do jogo, no estilo atual, e muda com o modo:
- **Textura:** a bandeja é pintada pelo mesmo pintor dos blocos (material + estilo), com a cor-base do material misturada à cor do modo (âmbar, verde-azulado, roxo ou cobre). O resultado é escurecido, para os blocos se destacarem.
- **Padrão por modo:**
  - Clássico: luz âmbar no alto;
  - Sudoku: listras diagonais verde-azuladas;
  - Tetris: linhas horizontais roxas;
  - História: cantos acobreados.
- **Casas vazias:** são buracos translúcidos rebaixados, com o tabuleiro aparecendo por baixo; uma casa sim, outra não, mais escura.
- **Realismo:** a textura cobre o tabuleiro inteiro.
- **Animação e Pixel:** a textura vira ladrilhos de 2×2 casas, alinhados com a grade.

Assim nenhuma combinação modo × material × estilo tem o mesmo tabuleiro.

## Raio e Eletricidade (v18)

- **Blocos:** mostram a eletricidade parada, um quadro de arcos por cima da textura. Não há animação por bloco: nas v16/v17 isso travava o celular.
- **Movimento:** fica no cenário (Tempestade e Usina Elétrica) e na moldura do tabuleiro.
- **Moldura:** três camadas transparentes desenhadas uma única vez:
  - arcos de borda A;
  - arcos de borda B;
  - para o Raio, um raio atravessando o tabuleiro.
- **Animação da moldura:** só a opacidade varia (`edge-a` 0,42 s, `edge-b` 0,58 s, `strike` 2,3 s). O chip de vídeo faz isso sem redesenhar nada.
- **Custo medido:** igual ao da Madeira.

## Menu e Ajustes (v18)

- **Texturas:** a escolha dos blocos fica só em Ajustes → Textura dos blocos (15 materiais, rolagem lateral). O menu inicial mostra só os modos.
- **História:** é o modo principal e tem borda neon acobreada pulsando devagar (só opacidade).

## Materiais novos (v19)

Mais 10 materiais (25 no total), todos nos três estilos, cada um com cenário, tabuleiro, explosão e fragmentos próprios:

| Material | Realismo | Explosão (Realismo / Animação / Pixel) | Cenário |
|---|---|---|---|
| Cristal | Malha de facetas lapidadas, brilhos | Cacos de vidro roxos + cintilação / gemas / cacos brilhantes | Caverna de Cristal |
| Ouro | Metal polido com reflexos e riscos finos | Lascas com borda dourada + moedas girando / moedas / quadradinhos que giram + faíscas em cruz | Templo Dourado |
| Neve | Montes macios com sombra azul, cristais | Pó de neve + flocos brilhando / flocos / grãos flutuando | Montanha Nevada |
| Nuvem | Cúmulo fofo sobre o céu | Sem pedaços: nuvens que se desfazem / nuvenzinhas subindo / blocos que sobem | Reino das Nuvens |
| Doce | Bala de vidro com listras de hortelã | Lascas + confeitos coloridos / balas embrulhadas / confeitos | Terra dos Doces |
| Galáxia | Espiral de nebulosa com núcleo brilhante | Sem pedaços: espiral de estrelas / brilhos em redemoinho / pixels piscando em espiral | Galáxia Espiral |
| Musgo | Pedra antiga com rachaduras e musgo | Torrões de pedra + folhas / tufos / torrões de terra | Ruínas Antigas |
| Vitral | Vidros coloridos com chumbo | Placas de vidro + faíscas coloridas / painéis / vidros coloridos | Catedral de Vitrais |
| Couro | Granulado, costura na borda | Rasga em tiras lentas / alças costuradas / tiras | Velho Oeste |
| Coral | Pólipos porosos | Torrões + bolhas subindo / galhos de coral / bolhas | Recife de Coral |

Os 10 cenários novos entraram nos mundos 18 a 27 da História. Depois vêm as versões Crepúsculo e Eclipse.

## Música (v18)

10 peças de domínio público, todas sintetizadas no jogo:
- Pachelbel – Cânone em Ré
- Bach – Prelúdio em Dó
- Satie – Gymnopédie nº 1
- Beethoven – Para Elise
- Beethoven – Ode à Alegria
- Brahms – Canção de Ninar
- Mozart – Pequena Serenata Noturna
- Grieg – Manhã
- Tradicional – Greensleeves
- Petzold – Minueto em Sol

As 7 novas têm percussão leve para servir também durante o jogo. Na História, a música muda a cada 10 fases, em rodízio de 12 faixas (as 10 acima mais Grieg – Na Gruta do Rei da Montanha e Mozart – Marcha Turca). O nome da música aparece ao entrar na fase.

## Desempenho (v17)

- **Fundo:** o cenário é redesenhado a cerca de 30 quadros por segundo, com resolução máxima de 1,25× (1,5× na Nebulosa); o Pixel continua em ¼. Os efeitos de explosão continuam a 60 quadros por segundo e em resolução cheia.
- **Camada de efeitos:** quando não há nada a desenhar, ela é limpa uma vez e fica parada.

## Explosões únicas por material (v15)

| Material | Realismo | Animação | Pixel |
|---|---|---|---|
| Madeira | Racha em 3 tábuas com pontas lascadas, giro rápido, lascas | Tábuas com veio | Ripas 4×1 que giram |
| Tijolo | Quebra limpa na argamassa (meio tijolo + 2 pedaços), poeira de argamassa | Tijolinhos com faixa de argamassa | Peças 2×1, pó de argamassa |
| Cimento | Esfarela em 9 torrões, nuvem de pó cinza | Pedrinhas + nuvens | Grão fino + pó subindo |
| Gelo | Estilhaça em 8 cacos finos de vidro, brilhos e névoa | Triângulos de gelo + estrelas | Cacos rápidos + brilho em cruz |
| Água | Sem pedaços: coroa de respingos para cima + ondas | Gotas lançadas para cima | Coroa de gotas |
| Lava | Gotas derretidas incandescentes, fogo e fumaça | Bolhas de lava com núcleo | Quadrados brilhantes + fumaça |
| Mármore | 4 lascas polidas grandes, brilho cintilante | Losangos + estrelas | Blocos grandes e lentos + brilho |
| Metal | Chapa rasgada em 2, borda espelhada, chuva de faíscas, "clang" | Porcas sextavadas + faíscas | Peças + faíscas em cruz |
| Raio | Raio cai do céu em cada linha, pedaços carbonizados com borda incandescente | Raiozinhos + descarga do céu | Raio de pixels do topo da tela |
| Eletricidade | Arcos em cadeia entre os blocos, pedaços que se desintegram | Orbes + arcos | Arco de pixels entre blocos |
| Chuva | Vira chuva: gotas e riscos caindo, névoa | Gotas caindo | Riscos de chuva caindo |
| Futuro | Voxels 3×3 que flutuam e piscam, barras de interferência | Quadrados neon | Quadrados neon piscando |
| Areia | Desmorona em torrões que encolhem, escorre como ampulheta | Grãos escorrendo | Grãos escorrendo |
| Grama | Torrões com terra embaixo, folhas e pétalas | Folhas + pétalas | Folhas 2×1 + torrões de terra |
| Fumaça | Sem pedaços: volutas que sobem e se dissolvem | Nuvenzinhas que sobem | Quadrados que sobem e crescem |

## Conteúdo e progressão (v22)

- **Metas variadas na História**, sem exigir linhas fora das fases 8/9:
  - fases …4: "Coloque N blocos";
  - fases …6: "Faça N jogadas sem perder";
  - fases …8 e …9: linhas de uma vez;
  - demais fases: pontos.
- **Próximas peças:** a próxima mão aparece em miniatura acima da bandeja (Clássico, Sudoku, História e Diário). Se nenhuma delas couber quando chegar a vez, é trocada.
- **Mapa da História:** cada mundo tem uma faixa com a cor do seu cenário e uma trilha de 20 paradas em zigue-zague, ligadas por uma linha tracejada. As paradas mostram a meta (pontos, ▪ blocos, ↻ jogadas, ×2/×3 linhas).
- **Conquistas:** 15 marcos de linhas, combos, linhas de uma vez, estrelas, medalhas, recordes, desafios do dia e texturas. Cada uma vale +1 estrela, que conta para liberar Mármore e Metal. Ao conquistar aparece um aviso no topo, e a lista fica em Recordes → Conquistas.
- **Desafio do dia:** botão "Diário" no menu. Uma fase gerada pela data, igual para todos e sem internet, sempre com meta de pontos (+20%), com desfazer e continuar. Guarda a sequência de dias seguidos, e o botão mostra ✓ quando o desafio do dia já foi feito.

## Ajudas ao jogador (v21)

- **Tutorial:** 3 telas ilustradas (arrastar peças, completar linhas, quadrados da História) na primeira partida. Pode ser revisto em Ajustes → Como jogar.
- **Desfazer (História):** botão ↶ ao lado de "voltar", com 2 usos por fase (o contador fica no botão). Também aparece na tela de derrota.
- **Continuar (História):** uma vez por fase, ao perder, limpa as 3 linhas mais cheias (sem pontos) e dá peças novas.
- **Modo leve (Ajustes):** o fundo é desenhado uma vez e fica parado, o limite de partículas cai para 320 e as animações em loop (moldura elétrica, neon, pulsos) param. Medido com o processador 6× mais lento: 19 → 61 quadros por segundo.

## História: metas e dificuldade (v20)

- **Fases terminadas em 8 e 9** (as últimas antes da troca de música): para vencer, é preciso destruir **2 linhas de uma vez** (fase …8) ou **3 linhas de uma vez** (fase …9). O tabuleiro já vem com 2 ou 3 linhas paralelas faltando a mesma casa, e a primeira peça da bandeja é a peça reta que encaixa ali.
- **Todas as outras fases:** fazer uma meta de pontos (60 a ~650).
- **Dificuldade em ondas:** em cada bloco de 10 fases, fácil, um pouco difícil, fácil de novo… e as fases 8 e 9 são o pico. A curva geral sobe devagar ao longo das 1000 fases e fica abaixo da antiga.
- **Clássico:** também em ondas, com um nível de descanso a cada 3, e teto de dificuldade menor.
- **Requisitos embaixo do tabuleiro, em três quadrados:**
  - Para vencer: a meta e o progresso;
  - Desafio ★: bônus que só dá estrela;
  - Poder.

  No topo ficam os pontos e o mundo.

## História: 1000 fases (v14)

- 50 mundos de 20 fases. A cada 20 fases o cenário muda.
- 17 cenários em ordem: Superfície, Campos Verdes, Céu, Floresta da Chuva, Tempestade, Deserto Dourado, Lua, Espaço Profundo, Nebulosa, Cidade do Futuro, Usina Elétrica, Vulcão, Vale da Fumaça, Mar Aberto, Abismo Oceânico, Vale Assombrado, Campo de Batalha.
- Depois os cenários voltam em versões "· Crepúsculo" e "· Eclipse" (outra luz e outras cores), então nenhum mundo repete o fundo.
- Cada cenário tem seu material de bloco; as versões Crepúsculo/Eclipse usam outras paletas.
- A dificuldade sobe dentro de cada mundo e cresce devagar de um mundo para o outro.
- Medalhas por mundo: ouro com 54 ★, prata com 36 ★ (de 60). Medalhas dos antigos mundos de 10 fases foram convertidas.

## História: mundos e materiais (v12, substituído pela v14)

| Mundo | Nome | Material dos blocos |
|---|---|---|
| 1 | Superfície | Madeira |
| 2 | Céu | Mármore |
| 3 | Lua | Cimento |
| 4 | Espaço Profundo | Metal |
| 5 | Cidade do Futuro | Gelo |
| 6 | Vulcão | Lava |
| 7 | Mar Aberto | Água |
| 8 | Abismo Oceânico | Água |
| 9 | Vale Assombrado | Cimento |
| 10 | Campo de Batalha | Tijolo |

Cada mundo alterna duas paletas entre as fases e gira a ordem das cores, então nenhuma fase repete as cores da anterior. O material da História é do mundo e não depende da textura escolhida no menu (que vale para Clássico e Sudoku).

## v23

- **Contra o Tempo:** 2 minutos com regras do Clássico (8×8); o relógio fica no HUD e pisca nos últimos 10 s. Recordes próprios (aba "Tempo").
- **Dicas:** depois de 20 s sem tocar na tela (Clássico, Sudoku e Contra o Tempo), uma casa boa para uma das peças pulsa de leve. Não aparece na História, no Diário nem no Tetris.
- **Chefes de mundo:** a fase 20 de cada mundo tem blocos de pedra (cinza, com moldura interna). A 1ª linha racha a pedra; a 2ª linha quebra. O poder também racha. Meta: quebrar todas as pedras. 👑 no mapa.
- **Loja de estrelas:** saldo = estrelas ganhas − gastas (desbloqueio de Mármore/Metal continua usando as ganhas). 3 músicas extras (Mozart, Rossini, Vivaldi), 5 molduras de tabuleiro, 3 efeitos (estrelas, arco-íris, fogos). Sem dinheiro real.
- **Música:** a do menu e a do jogo são escolhidas separadamente em Ajustes ("Automática" = a de cada modo, e a História troca a cada 10 fases).
- **Perfil:** tempo jogado, linhas no total e material favorito, junto das conquistas.
- **Progresso em arquivo:** Ajustes → Salvar arquivo / Restaurar (JSON `{app, v, date, data}`), pelo seletor de arquivos do Android, sem internet.
- **Vibração:** padrões mais fortes nos combos ×2/×3 e na vitória (amplitude máxima no Android).
- **Textos:** tamanhos de 9–11,5 px subiram para 10,5–13 px.
- **Identidade:** ícone com tabuleiro escuro, borda neon cobre da História e um bloco de cada material; tela de abertura com a mesma moldura neon e blocos texturizados.

## v24

- **Guardar peça:** espaço "Guardar" ao lado das próximas peças. Arraste uma peça até ele, ou toque na peça e depois em Guardar. Depois de guardar, é preciso colocar uma peça antes de guardar de novo. Na História vale a partir do Mundo 2. Não existe no Tetris nem no Quebra-cabeça.
- **Girar:** uma vez por fase da História, gira uma peça 90° (toque em Girar e depois na peça).
- **Perigo:** com 3 ou menos encaixes possíveis, o tabuleiro brilha em vermelho, a música acelera um pouco e toca um batimento.
- **Mão amigável:** depois de perder 3 vezes na mesma fase, aparece "Tentar com ajuda 🍀" (peças mais fáceis nessa tentativa).
- **Chefes variados** (a fase 20 de cada mundo, em ciclo): pedra (2 linhas para quebrar), gelo (aparece um novo a cada 5 jogadas), bombas (contador; limpe a linha antes do zero) e fantasmas (mudam de casa a cada 2 jogadas).
- **Quebra-cabeça:** 24 desenhos × 2 cortes = 48. As peças cobrem o desenho exatamente; não há linhas. Desfazer ilimitado; a dica mostra o lugar certo. Cada desenho novo vale +1 ★.
- **Diário:** sequência de dias 🔥 (7 dias seguidos = +3 ★) e 3 missões por semana (+2 ★ cada), sorteadas pela semana do calendário.
- **Loja:** novas abas Formatos (quadradão, cantos suaves, bolinhas, gemas), Sons (sino, piano, tambor), Placar (clássico, digital, desenhado) e Fundos (pôr do sol, noite estrelada, aurora).
- **Abertura de mundo:** cartão grande com número, nome, textura e música do mundo.
- **Placar:** os números sobem contando; ao bater o recorde, o placar brilha e solta confete.
- **Acessibilidade:** modo daltônico (símbolos ●▲■◆★✚ nas cores), texto grande e modo canhoto (peça à direita do dedo, botões invertidos).


## v25

- Backup automático removido (a pedido). Continua o salvar/restaurar manual em Ajustes → Progresso.

## v26

- **Sem retomar partidas:** sair e voltar (ou fechar o app) sempre começa um tabuleiro novo, em todos os modos.
- **Recomeçar:** botão ↻ no topo de toda partida (e no menu de pausa). Apaga as peças colocadas e volta o tabuleiro ao início da partida/fase, com confirmação.
- **Pausa em todos os modos:** botão ⏸ no topo. O menu de pausa tem Continuar, Recomeçar, Sair da partida e os botões de música e efeitos sonoros (que saíram do topo).
- **Confirmar saída:** voltar ou sair no meio de uma partida pergunta antes ("Sair da partida?").
- **História:** os mundos 2, 5, 8, 11 … (a cada 3) usam o tabuleiro Sudoku 9×9; os outros, o clássico 8×8. Etiqueta "▦ Sudoku" no mapa.
- **Chefe final:** a fase 20 dos mundos 10, 20, 30, 40 e 50 junta 3 pedras, 2 bombas (contador 14+) e 2 fantasmas; menos blocos soltos no tabuleiro. 🐉 no mapa.
- **Dicas por modo (uma vez):** Sudoku (modo ou primeiro mundo Sudoku), Contra o Tempo, Quebra-cabeça, Diário e cada tipo de chefe.
- **Quebra-cabeça:** +24 desenhos em temas — Animais, Letras e Objetos (15 em 9×9). Total 72. Cada tema libera 4 de cada vez.
- **Música que reage:** a partir do combo ×2 entra a melodia uma oitava acima (harpa); do ×4 entra uma batida extra. Saem suavemente quando o combo acaba.
- **Som por textura:** cada um dos 25 materiais tem impacto, detritos, escala e efeito próprios (madeira bate, gelo tilinta, lava ronca, raio estala, ouro faz moeda…), tudo sintetizado — o APK quase não cresce.
- **Altura da peça ao arrastar:** Perto, Médio ou Longe, em Ajustes.
- **Deitado e tablet:** o app gira com o celular. Deitado, o tabuleiro fica à esquerda e as peças à direita; o menu vira duas colunas. Tablet em pé usa coluna mais larga e casas maiores.

## v27

- **Itens especiais (História, a partir do Mundo 2):** 💣 Bomba (explode 3×3, até pedras e blocos de chefe), ⛏️ Picareta (quebra qualquer bloco de uma vez) e 🌈 Arco-íris (completa a linha tocada). Ganhos na 1ª vitória das fases 5, 10, 15 e 20 de cada mundo (a fase 20 do Mundo 1 dá 1 de cada). Máximo 9 de cada. Botão 🎒 ao lado de Girar. Se travar com itens na mochila, o jogo avisa em vez de acabar.
- **Clima nos cenários:** 18 partículas só com transform/opacity (GPU), conforme o material: chuva, neve, folhas, brasas, bolhas, poeira, brilhos, faíscas, cinzas e pétalas. Desliga no Modo leve e tem chave em Ajustes. Custo medido: ~5 fps com CPU 6× mais lenta.
- **Chefe derrotado:** a explosão final em câmera lenta (0,28×), zoom e brilho no tabuleiro, fanfarra própria. **Chefe final:** além disso, show de luzes com feixes coloridos girando, 7 fogos de artifício e confete.
- **Desfazer no Clássico e no Sudoku:** ganha 1 a cada 500 pontos, acumula até 3.
- **Volume:** controles separados de Música, Efeitos e Vibração (0–100%) em Ajustes.

## v28

- Itens especiais: máximo 3 de cada (estoques maiores são reduzidos para 3).
- Botões do menu (Recordes, Desafio diário, Loja, Ajustes) com fundo escuro sólido: legíveis em cenários claros (neve, nuvens).
- "Diário" passa a se chamar **Desafio diário**.
- **Fases que giram** (contadas dentro de cada bloco de 100 fases da História: 7, 107, 207…):
  - Subchefe ⚡ — 7, 13, 29, 43, 61, 71, 83, 93: névoa com "FASE DESAFIANTE!" ao começar, a tela treme a cada 3 jogadas e o tabuleiro gira 90° para a direita a cada 5.
  - 30, 60, 90: gira 90° para a direita a cada 5 jogadas.
  - 10, 20, 40, 60, 80: a cada 5 jogadas o tabuleiro treme (com vibração) e gira 90° para a esquerda.
  - A fase 60 está nas duas listas: treme e alterna direita/esquerda.
  - 🌀 marca essas fases no mapa. Os blocos (e os contadores das bombas) giram junto com o tabuleiro.

## v29

Regras da História pelo número real da fase (somam com as da v28; se duas caem na mesma jogada, acontecem em sequência e as peças esperam o tabuleiro parar):
- **Múltiplos de 4 a partir da 8:** gira 90° no sentido horário a cada 4 jogadas, sem aviso.
- **Múltiplos de 13 a partir da 13:** começa com 4 casas trancadas 🔒 (nenhuma peça entra); abrem depois da 2ª jogada, liberadas para a 3ª.
- **Múltiplos de 6 a partir da 6:** meia-volta (180°) a cada 5 jogadas.
- **Múltiplos de 9 a partir da 9:** espelha na horizontal (esquerda ↔ direita) a cada 5 jogadas.
- **Múltiplos de 7 a partir da 17:** espelha na vertical (cima ↔ baixo) a cada 7 jogadas.

## v30 — Block Super GF

- **Novo nome:** BLOCK SUPER GF (título, logo, abertura, nome do app e do APK, arquivo de progresso). Os identificadores internos (pacote Android, chave de assinatura, armazenamento) continuam os mesmos para o app atualizar por cima e manter o progresso. Arquivos de progresso antigos ("BLOCK ALT UNO") continuam sendo aceitos.
- **Super Desafio ⚔️** (cartão roxo no menu): corridas no estilo Hades.
  - *Corrida de fases:* meta 120 pontos na fase 1, +60 por fase. Ao vencer, 2 cartas de habilidade; escolhe-se 1. Elas se acumulam até a corrida acabar. Recorde: fase alcançada e total.
  - *Sobrevivência:* um tabuleiro só; a cada 20 jogadas, 2 cartas. Recorde: pontos.
  - Cartas 🟢 leves (+10% pontos), 🟡 médias (+20%), 🔴 pesadas (+35%); as pesadas só depois da fase 6. Habilidades periódicas podem vir de novo (nível 2 e 3: 1 jogada a menos). A cada 5 escolhas pode aparecer um **Alívio 🍀** que remove uma habilidade.
  - Faixa roxa acima do tabuleiro mostra as habilidades ativas (tocar abre a lista) e o multiplicador de pontos. 1 desfazer por fase (some com "Sem desfazer").
  - **50 habilidades:** giro horário, giro anti-horário, espelho lateral, de cabeça para baixo, correnteza, terremoto, bloco intruso, bloco sumido, bloco andarilho, bloco teimoso, pedreira, bomba-relógio, assombração, geada, cadeado, muro, peças grandes, sem retinhas, mão curta, às cegas, peça surpresa, peça torta, sem Guardar, bandeja trocada, linha preguiçosa, só linhas, só colunas, combo frágil, meta inflada, imposto, relógio, pressa, contagem, neblina, tremedeira, tabuleiro menor (7×7), tabuleiro Sudoku, monocromático, sem desfazer, dedo espelhado, maré, peças fantasmas, desgaste, meta dupla, bandeja giratória, mão pesada, sem combo, apagão, **bomba-relógio invertida** (casa sinalizada que, ao zerar, enche as vizinhas de blocos) e **diminuição de jogadas** (tudo que demora X jogadas passa a demorar X−1; acumula, mínimo 2, vale também para habilidades futuras, bombas e cadeados).

## v31

- **Guardar** só existe no modo **Clássico** (saiu da História, Desafio diário, Sudoku, Contra o Tempo e Super Desafio).
- Correção: soltar uma peça nas casas de baixo à esquerda (logo acima do botão Guardar) às vezes guardava a peça em vez de colocá-la. Agora, se a peça cabe no tabuleiro, ela é colocada; o Guardar só recebe a peça quando ela não está sobre uma posição válida.
- Super Desafio: a habilidade "Sem Guardar" virou **🚫 Sem prévia** (ao arrastar, o tabuleiro não mostra onde a peça vai cair).
- Menu: o cartão do **Super Desafio** tem chamas animadas na borda (só transform/opacity; paradas no Modo leve). O cartão da **História** ganhou neon amarelo fluorescente.

## v32

- **Fogo realista no Super Desafio:** as chamas são desenhadas num canvas em volta do cartão: ~400 partículas finas e altas, com brilho somado (como fogo real no escuro), núcleo branco-amarelo → laranja → vermelho → fumaça, ondulando e subindo, mais fortes no topo, além de faíscas e da borda incandescente. O fogo fica só do lado de fora do cartão (não cobre o texto). Roda a ~30 fps e só com o menu na tela; no Modo leve vira uma imagem parada. Custo medido: ~1 fps com CPU 6× mais lenta.
- **Configurações:** abrem sempre no topo (antes o foco no botão "Pronto" rolava a janela até o fim) e o título "Configurações" fica mais abaixo, com espaço acima. Loja, Desafio diário, Quebra-cabeça, Itens e Super Desafio ganharam a mesma margem.

## v33

- **Fogo do Super Desafio refeito por dentro do cartão:** as chamas nascem na borda de baixo e sobem pelas laterais, só dentro do cartão (fundo escuro), sem passar por cima dos outros cartões. O texto ganhou sombra para continuar legível.
- **Título com neon forte:** "BLOCK" em rosa-choque e "SUPER GF" em ciano elétrico, letras claras com contorno neon e brilho em camadas, com uma piscada sutil de letreiro (parada no Modo leve).
- **Blocos acima do título** não são mais cortados ao subir.
- **Quebra-cabeça mais fácil:** tabuleiro maior (10×10 ou 11×11) com o desenho marcado em rosa no meio. As peças podem ir para qualquer lugar; as já colocadas podem ser **arrastadas** para outro lugar ou **tocadas** para voltar à bandeja. Linhas completas somem (as peças atingidas voltam inteiras para a bandeja, então o desenho nunca fica impossível). Vence quando todas as casas rosa estão cobertas. Blocos fora do desenho ficam com contorno tracejado. O modo nunca acaba sozinho.
- **Desfazer ilimitado** só no Quebra-cabeça (o contador mostra ∞).

## v34

- **Quebra-cabeça — desfazer ilimitado de verdade:** cada jogada fica guardada e dá para desfazer uma atrás da outra até o começo do desenho.
- **Casas fora do desenho jogáveis:** as casas normais ficam com aparência normal (sem escurecer nem tracejado) e aceitam peças. Se as peças acabarem com casas rosa vazias, o jogo entrega peças pequenas novas para terminar o desenho.
- **Estrelas:** o desenho termina quando todas as casas rosa estão cobertas. **5 ★** se só as casas rosa estiverem preenchidas; **1 ★** se sobrar bloco em casa normal. Dá para voltar e melhorar de 1 para 5 (ganha a diferença). A grade de desenhos mostra 1★ ou 5★ em cada um.
