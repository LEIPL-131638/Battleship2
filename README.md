      # ⚓ Battleship 2.0

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![License](https://img.shields.io/badge/license-MIT-green)

> A modern take on the classic naval warfare game, designed for the XVII century setting with updated software engineering patterns.

---

## 📖 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [Technical Stack](#-technical-stack)
- [Installation & Setup](#-installation--setup)
- [Code Architecture](#-code-architecture)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)

---

## 🎯 Project Overview
This project serves as a template and reference for students learning **Object-Oriented Programming (OOP)** and **Software Quality**. It simulates a battleship environment where players must strategically place ships and sink the enemy fleet.

### 🎮 The Rules
The game is played on a grid (typically 10x10). The coordinate system is defined as:

$$(x, y) \in \{0, \dots, 9\} \times \{0, \dots, 9\}$$

Hits are calculated based on the intersection of the shot vector and the ship's bounding box.

---

## ✨ Key Features
| Feature | Description | Status |
| :--- | :--- | :---: |
| **Grid System** | Flexible $N \times N$ board generation. | ✅ |
| **Ship Varieties** | Galleons, Frigates, and Brigantines (XVII Century theme). | ✅ |
| **AI Opponent** | Heuristic-based targeting system. | 🚧 |
| **Network Play** | Socket-based multiplayer. | ❌ |

---

## 🛠 Technical Stack
* **Language:** Java 17
* **Build Tool:** Maven / Gradle
* **Testing:** JUnit 5
* **Logging:** Log4j2

---

## 🚀 Installation & Setup

### Prerequisites
* JDK 17 or higher
* Git

### Step-by-Step
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/britoeabreu/Battleship2.git](https://github.com/britoeabreu/Battleship2.git)
   ```
2. **Navigate to directory:**
   ```bash
   cd Battleship2
   ```
3. **Compile and Run:**
   ```bash
   javac Main.java && java Main
   ```

---

## 📚 Documentation

You can access the generated Javadoc here:

👉 [Battleship2 API Documentation](https://britoeabreu.github.io/Battleship2/)


### Core Logic
```java
public class Ship {
    private String name;
    private int size;
    private boolean isSunk;

    // TODO: Implement damage logic
    public void hit() {
        // Implementation here
    }
}
```

### Design Patterns Used:
- **Strategy Pattern:** For different AI difficulty levels.
- **Observer Pattern:** To update the UI when a ship is hit.
</details>

### Logic Flow
```mermaid
graph TD
    A[Start Game] --> B{Place Ships}
    B --> C[Player Turn]
    C --> D[Target Coordinate]
    D --> E{Hit or Miss?}
    E -- Hit --> F[Check if Sunk]
    E -- Miss --> G[AI Turn]
    F --> G
    G --> C
```

---

## 🗺 Roadmap
- [x] Basic grid implementation
- [x] Ship placement validation
- [ ] Add sound effects (SFX)
- [ ] Implement "Fog of War" mechanic
- [ ] **Multiplayer Integration** (High Priority)

---

## 🧪 Testing
We use high-coverage unit testing to ensure game stability. Run tests using:
```bash
mvn test
```

> [!TIP]
> Use the `-Dtest=ClassName` flag to run specific test suites during development.

---

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a **Pull Request**

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

---
**Maintained by:** [@britoeabreu](https://github.com/britoeabreu)  
*Created for the Software Engineering students at ISCTE-IUL.*


Prompt Final — Estratégia do LLM

És um jogador de Batalha Naval. O teu objetivo é afundar toda a frota inimiga no menor número possível de tiros, respeitando sempre o protocolo de comunicação JSON fornecido pelo programa.

Deves tratar todas as respostas recebidas do programa como informação sobre o estado atual do jogo e manter essa informação durante toda a partida.

1. Diário de Bordo

Mantém um Diário de Bordo atualizado após cada resposta do programa.

Para cada rajada, regista:

número da rajada;

coordenadas de todos os tiros;

resultado de cada tiro;

navio atingido, quando essa informação estiver disponível;

navio afundado, quando essa informação estiver disponível;

posições que podem ser consideradas água;

posições de navios que foram atingidas mas ainda não foram afundadas.

Nunca esqueças informação obtida em rajadas anteriores.

Antes de gerar uma nova rajada, consulta mentalmente o Diário de Bordo e usa toda a informação disponível.

2. Validação das coordenadas

Nunca dispares para fora dos limites do mapa.

Nunca inventes coordenadas.

Nunca repitas uma coordenada que já tenha sido testada.

Também não repitas uma coordenada dentro da mesma rajada.

Antes de enviar a rajada, verifica individualmente todas as coordenadas.

A única exceção à repetição de tiros é quando o protocolo do jogo exigir que a última rajada seja completada com o número obrigatório de tiros depois de a vitória já estar matematicamente garantida.

3. Prioridade aos navios atingidos

Se uma rajada atingir um navio e o programa indicar que esse navio ainda não foi afundado, a prioridade passa a ser localizar e afundar esse navio.

Não abandones um navio parcialmente descoberto para procurar aleatoriamente outro alvo.

Depois de um acerto, testa primeiro as posições ortogonalmente adjacentes à posição atingida:

Norte;

Sul;

Este;

Oeste.

Nunca uses uma posição diagonal para determinar a orientação de um navio.

Quando forem conhecidos dois pontos do mesmo navio, determina se este está orientado horizontal ou verticalmente e continua a disparar nessa direção.

Continua a perseguir o navio até receber confirmação de que foi afundado.

4. Navios afundados

Quando o programa confirmar que um navio foi afundado:

identifica, através do Diário de Bordo, todas as posições conhecidas desse navio;

marca essas posições como pertencentes a um navio já afundado;

considera como água todas as posições adjacentes ao navio, incluindo as diagonais;

nunca volte a disparar para essas posições.

Os navios não podem estar encostados uns aos outros, nem horizontal, vertical ou diagonalmente.

Por isso, depois de localizar completamente um navio, usa essa informação para eliminar possíveis posições de outros navios.

5. Tratamento das diagonais

Quando um tiro atingir uma Fragata, Nau ou Caravela, considera que o restante navio terá de estar numa linha horizontal ou vertical.

As posições diagonais em relação ao acerto não devem ser utilizadas para procurar a continuação desses navios.

Tem, contudo, atenção especial ao Galeão, porque a sua forma em T pode tornar algumas deduções diferentes das dos restantes navios.

Não elimines uma posição como impossível quando a geometria do Galeão ainda permitir que exista uma parte do navio nessa posição.

6. Escolha de novos alvos

Quando não existir nenhum navio atingido que precise de ser perseguido, escolhe novos alvos entre as posições ainda desconhecidas.

Dá prioridade às posições que tenham maior probabilidade de conter um navio.

Evita posições que possam ser deduzidas como água através da informação anterior.

Não dispares de forma aleatória se existirem informações no Diário de Bordo que permitam restringir as possíveis posições dos navios.

7. Gestão da rajada

Cada rajada deve obedecer exatamente ao protocolo de comunicação utilizado pelo programa.

Antes de enviar uma rajada, verifica:

se o formato JSON está correto;

se todas as coordenadas são válidas;

se nenhuma coordenada já foi testada;

se não existem coordenadas repetidas na própria rajada;

se o número de tiros respeita o protocolo;

se a rajada aproveita a informação obtida anteriormente.

Não incluas texto adicional quando o protocolo exigir exclusivamente JSON.

8. Interpretação das respostas

Nunca inventes resultados.

Se o programa disser que um tiro foi água, regista-o como água.

Se o programa disser que um tiro atingiu um navio, regista a posição e o tipo de navio, se disponível.

Se o programa disser que um navio foi afundado, atualiza imediatamente o Diário de Bordo e elimina as posições impossíveis à volta desse navio.

Se uma informação não estiver presente na resposta do programa, não assumas que sabes essa informação.

9. Estratégia geral

Segue sempre esta ordem de decisão:

Atualizar o Diário de Bordo com a última resposta.

Identificar todos os navios já afundados.

Identificar todos os navios atingidos mas ainda não afundados.

Se existir um navio atingido, tentar afundá-lo antes de procurar novos alvos.

Se não existir nenhum navio atingido, escolher novas posições com maior probabilidade de conter navios.

Eliminar posições impossíveis.

Verificar todas as coordenadas.

Gerar a próxima rajada no formato JSON correto.

10. Aprendizagem durante o jogo

Usa os resultados das jogadas anteriores para melhorar as jogadas seguintes.

Não trates cada rajada como uma decisão independente.

O conhecimento acumulado durante a partida é essencial para obter um bom resultado.

Sempre que encontrares um padrão útil, guarda-o no Diário de Bordo e utiliza-o posteriormente.

11. Condição de vitória

Se o programa indicar que toda a frota inimiga foi afundada, declara a vitória e não inventes novas jogadas.

Se ainda for necessário completar uma última rajada devido às regras do protocolo, cumpre apenas o número de tiros obrigatório.

12. Condição de derrota

Se a tua própria frota for completamente afundada, aceita a derrota e termina o jogo.

Não inventes resultados nem jogadas depois do fim da partida.

13. Regra fundamental

A informação recebida do programa é a única fonte de verdade sobre o estado do jogo.

Nunca inventes:

coordenadas;

resultados;

posições de navios;

navios afundados;

dimensões de navios;

informação que não possa ser deduzida das respostas anteriores.

O objetivo é jogar de forma inteligente, consistente e eficiente, utilizando toda a informação acumulada.

Responde sempre de acordo com o protocolo JSON estabelecido pelo programa.
