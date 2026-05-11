# Guía: Usar OpenCode con Telegram

## 1. Requisitos

- Node.js instalado
- OpenCode instalado (`npm install -g opencode-ai`)
- Token de bot de Telegram (de [@BotFather](https://t.me/BotFather))
- Tu ID numérico de Telegram

## 2. Instalar el bot de Telegram

```bash
npx @grinev/opencode-telegram-bot
```

O instalación global:

```bash
npm install -g @grinev/opencode-telegram-bot
opencode-telegram start
```

## 3. Configurar variables de entorno

Crea un archivo `.env`:

```
TELEGRAM_BOT_TOKEN=tu_token_de_bot
TELEGRAM_USER_ID=tu_id_numerico
OPENCODE_API_URL=http://localhost:4096
OPENCODE_MODEL_PROVIDER=opencode
OPENCODE_MODEL_ID=big-pickle
```

## 4. Iniciar OpenCode server

```bash
opencode serve
```

Esto inicia el servidor HTTP en `http://localhost:4096`.

## 5. Iniciar el bot de Telegram

```bash
opencode-telegram start
```

## 6. Comandos disponibles en Telegram

| Comando | Descripción |
|---------|-------------|
| `/status` | Estado del servidor, proyecto, sesión y modelo |
| `/new` | Crear nueva sesión |
| `/stop` | Cancelar tarea actual |
| `/sessions` | Navegar entre sesiones recientes |
| `/projects` | Cambiar de proyecto |
| `/rename` | Renombrar sesión actual |
| `/help` | Mostrar ayuda |
| `/opencode_start` | Iniciar servidor OpenCode remoto |
| `/opencode_stop` | Detener servidor OpenCode remoto |

## 7. Seleccionar modelo

Para cambiar de modelo:

1. Presiona el botón **"Model"** en el teclado inferior de Telegram
2. Selecciona el modelo deseado de tu lista de favoritos

Para añadir modelos a favoritos:
1. Abre OpenCode TUI localmente: `opencode`
2. Escribe `/models`
3. Navega al modelo y presiona **Ctrl+F**
4. Los favoritos aparecerán en el selector del bot

---

# Crear Subagentes

## Subagente de Chat General

Crea un archivo `.opencode/agents/chat.md`:

```markdown
---
name: chat
description: Asistente de chat general para responder preguntas y conversar
mode: subagent
model: opencode/big-pickle
---

Eres un asistente de chat amigable y servicial. Responde preguntas, explica conceptos y ayuda al usuario con dudas generales. Mantén las respuestas claras y concisas.
```

## Subagente de Revisión de Código

Crea `.opencode/agents/review.md`:

```markdown
---
name: review
description: Revisor de código que analiza calidad, bugs y mejores prácticas
mode: subagent
model: opencode/gpt-5.1-codex
---

Eres un revisor de código experto. Tu tarea es analizar el código proporcionado y detectar:

- Bugs y errores lógicos
- Problemas de seguridad
- Malas prácticas
- Oportunidades de optimización
- Problemas de legibilidad y mantenibilidad

Proporciona retroalimentación clara con sugerencias específicas de mejora.
```

## Subagente de Planeación

Crea `.opencode/agents/plan.md`:

```markdown
---
name: plan
description: Planificador que descompone tareas en pasos accionables
mode: subagent
model: opencode/gpt-5.1-codex
---

Eres un planificador experto. Tu tarea es:

1. Analizar los requisitos del usuario
2. Descomponer el trabajo en tareas pequeñas y manejables
3. Identificar dependencias entre tareas
4. Estimar el esfuerzo relativo de cada tarea
5. Proponer un orden de implementación lógico

NO escribas código. Solo crea planes detallados.
```

## Subagente de Escritura de Código

Crea `.opencode/agents/code.md`:

```markdown
---
name: code
description: Ingeniero de software que escribe código limpio y funcional
mode: subagent
model: opencode/gpt-5.1-codex
---

Eres un ingeniero de software experto. Tu tarea es:

- Escribir código limpio, eficiente y bien estructurado
- Seguir las mejores prácticas del lenguaje y framework usado
- Manejar errores adecuadamente
- Escribir código legible y mantenible
- Seguir el estilo y convenciones del proyecto existente

Siempre explica brevemente qué hace tu código antes de escribirlo.
```

## Cómo usar los subagentes

### Desde Telegram

Simplemente escribe tu prompt normal. El bot lo enviará al agente principal de OpenCode.

Para usar un subagente específico, menciónalo en tu prompt:

```
@review revisa este código:
function sum(a, b) { return a + b; }
```

### Desde el TUI local

```bash
opencode --agent review "Revisa el archivo src/index.ts"
```

O dentro del TUI escribe directamente tu mensaje y el agente se seleccionará automáticamente según la configuración.

### Asignar agentes por proyecto

En `opencode.json`:

```json
{
  "agent": {
    "default": "chat",
    "review": { "model": "opencode/gpt-5.1-codex" },
    "plan": { "model": "opencode/gpt-5.3-codex" },
    "code": { "model": "opencode/gpt-5.1-codex" }
  }
}
```

## Flujo de trabajo recomendado

1. **Plan** → Describe lo que quieres construir
2. **Review** → Revisa el plan creado
3. **Code** → Implementa el plan
4. **Review** → Revisa el código implementado
