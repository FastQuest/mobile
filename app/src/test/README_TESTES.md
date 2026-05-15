# 🧪 Guia de Execução de Testes - FastQuest

Este guia explica como executar os testes unitários do projeto FastQuest.

## 📋 Pré-requisitos

- Android Studio instalado
- Projeto sincronizado com Gradle
- JDK 11 ou superior configurado

## 🚀 Métodos de Execução

### 1️⃣ Via Terminal/Linha de Comando

#### Executar TODOS os testes unitários:
```bash
./gradlew test
```

#### Executar testes com relatório detalhado:
```bash
./gradlew test --info
```

#### Executar testes de uma classe específica:
```bash
# Teste da tela de Login
./gradlew test --tests Screen1Test

# Teste da tela Home
./gradlew test --tests HomeScreenTest

# Teste de navegação
./gradlew test --tests AppNavigationTest
```

#### Executar um teste específico:
```bash
./gradlew test --tests Screen1Test."login screen should have correct text labels"
```

#### Executar testes e gerar relatório HTML:
```bash
./gradlew test
# O relatório será gerado em: app/build/reports/tests/testDebugUnitTest/index.html
```

#### Limpar e executar testes:
```bash
./gradlew clean test
```

---

### 2️⃣ Via Android Studio (Interface Gráfica)

#### Opção A: Executar todos os testes
1. Abra o Android Studio
2. No painel esquerdo, navegue até: `app/src/test/java/com/example/fastquest`
3. Clique com botão direito na pasta `test`
4. Selecione **"Run 'Tests in 'fastquest.test''"**
5. Os resultados aparecerão na aba "Run" na parte inferior

#### Opção B: Executar testes de uma classe específica
1. Abra o arquivo de teste desejado (ex: `Screen1Test.kt`)
2. Clique no ícone ▶️ verde ao lado do nome da classe
3. Selecione **"Run 'Screen1Test'"**

#### Opção C: Executar um teste individual
1. Abra o arquivo de teste
2. Localize o método de teste específico (ex: `fun login screen should have correct text labels()`)
3. Clique no ícone ▶️ verde ao lado do método
4. Selecione **"Run 'login screen should...'"**

#### Opção D: Executar com cobertura de código
1. Clique com botão direito na pasta `test`
2. Selecione **"Run 'Tests in 'fastquest.test'' with Coverage"**
3. Veja o relatório de cobertura na aba "Coverage"

---

### 3️⃣ Via Menu do Android Studio

1. Menu superior: **Run** → **Edit Configurations...**
2. Clique no **+** (Add New Configuration)
3. Selecione **JUnit**
4. Configure:
   - **Name**: "All Unit Tests"
   - **Test kind**: "All in package"
   - **Package**: `com.example.fastquest`
   - **Search for tests**: "In whole project"
5. Clique em **Apply** e **OK**
6. Execute via **Run** → **Run 'All Unit Tests'**

---

## 📊 Visualizando Resultados

### No Terminal:
```
> Task :app:testDebugUnitTest

Screen1Test > login screen should have correct text labels() PASSED
Screen1Test > email validation should work correctly() PASSED
Screen1Test > password should not be empty for valid login() PASSED
...

BUILD SUCCESSFUL in 5s
```

### No Android Studio:
- ✅ Verde = Teste passou
- ❌ Vermelho = Teste falhou
- ⚠️ Amarelo = Teste ignorado

### Relatório HTML:
Após executar `./gradlew test`, abra:
```
app/build/reports/tests/testDebugUnitTest/index.html
```

---

## 📁 Estrutura dos Testes

```
app/src/test/java/com/example/fastquest/
├── navigation/
│   └── AppNavigationTest.kt          (14 testes)
├── ui/
│   ├── components/
│   │   └── CommonComponentsTest.kt   (15 testes)
│   └── screens/
│       ├── Screen1Test.kt            (6 testes)
│       ├── Screen2Test.kt            (7 testes)
│       ├── HomeScreenTest.kt         (9 testes)
│       ├── QuestionScreenTest.kt     (13 testes)
│       ├── FolderDetailsScreenTest.kt (14 testes)
│       └── CreateScreenTest.kt       (12 testes)
```

**Total: 90+ testes unitários**

---

## 🔍 Comandos Úteis

### Ver apenas testes que falharam:
```bash
./gradlew test --tests "*Test" --continue
```

### Executar testes em modo debug:
```bash
./gradlew test --debug-jvm
```

### Executar testes de um pacote específico:
```bash
./gradlew test --tests "com.example.fastquest.ui.screens.*"
```

### Executar testes e parar no primeiro erro:
```bash
./gradlew test --fail-fast
```

---

## 🐛 Troubleshooting

### Problema: "Task :app:test FAILED"
**Solução:**
```bash
./gradlew clean
./gradlew test --stacktrace
```

### Problema: "Cannot find symbol"
**Solução:**
1. Sincronize o projeto: **File** → **Sync Project with Gradle Files**
2. Rebuild: **Build** → **Rebuild Project**

### Problema: Testes não aparecem no Android Studio
**Solução:**
1. Invalide o cache: **File** → **Invalidate Caches / Restart**
2. Reimporte o projeto

---

## 📈 Boas Práticas

1. **Execute os testes antes de fazer commit:**
   ```bash
   ./gradlew test
   ```

2. **Execute testes relacionados após mudanças:**
   ```bash
   # Mudou a HomeScreen? Execute:
   ./gradlew test --tests HomeScreenTest
   ```

3. **Verifique a cobertura periodicamente:**
   - Android Studio: Run with Coverage
   - Ou use JaCoCo para relatórios detalhados

4. **Mantenha os testes rápidos:**
   - Testes unitários devem executar em segundos
   - Se demorar muito, considere otimizar

---

## 🎯 Exemplos Práticos

### Exemplo 1: Testar após modificar tela de Login
```bash
./gradlew test --tests Screen1Test
```

### Exemplo 2: Testar toda a navegação
```bash
./gradlew test --tests AppNavigationTest
```

### Exemplo 3: Testar todos os componentes
```bash
./gradlew test --tests CommonComponentsTest
```

### Exemplo 4: Executar testes e abrir relatório automaticamente (macOS/Linux)
```bash
./gradlew test && open app/build/reports/tests/testDebugUnitTest/index.html
```

### Exemplo 5: Executar testes e abrir relatório automaticamente (Windows)
```bash
./gradlew test && start app/build/reports/tests/testDebugUnitTest/index.html
```

---

## 📞 Suporte

Se encontrar problemas:
1. Verifique se o Gradle está sincronizado
2. Limpe o projeto: `./gradlew clean`
3. Rebuild: `./gradlew build`
4. Verifique as dependências no `build.gradle.kts`

---

## ✅ Checklist Rápido

Antes de fazer commit/push:
- [ ] Executei `./gradlew test`
- [ ] Todos os testes passaram ✅
- [ ] Adicionei testes para novas funcionalidades
- [ ] Atualizei testes existentes se necessário

---

**Última atualização:** 2026-05-15
**Versão:** 1.0