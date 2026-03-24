# Automatización de Pruebas Funcionales – ParaBank

Este repositorio contiene la implementación de una simulación de **automatización de pruebas funcionales** utilizando **Selenium WebDriver**, **Java** y **JUnit**, aplicada a la aplicación web de pruebas **ParaBank**.

El proyecto fue desarrollado como parte del curso **Calidad de Software**, con el objetivo de demostrar la implementación práctica de pruebas automatizadas sobre funcionalidades críticas de un sistema web.

---

## Herramientas utilizadas

- Java
- Selenium WebDriver
- JUnit
- Maven
- Eclipse IDE
- Google Chrome

---

## Aplicación bajo prueba

**ParaBank**  
https://parabank.parasoft.com/parabank/index.htm

ParaBank es una aplicación web de demostración utilizada comúnmente para prácticas de pruebas de software y automatización.

---

## Casos de prueba automatizados

El proyecto incluye la automatización de los siguientes casos de prueba:

| ID | Caso de Prueba |
|---|---|
| TC-001 | Verificar navegación al formulario de registro |
| TC-002 | Registro exitoso de un nuevo usuario |
| TC-003 | Inicio de sesión exitoso |
| TC-004 | Apertura de una nueva cuenta |
| TC-005 | Visualización de cuentas en Accounts Overview |
| TC-006 | Transferencia exitosa entre cuentas |
| TC-007 | Validación de transferencia con monto superior al saldo disponible |

---

## Estructura del proyecto

CalidadSimulacion
│
├── pom.xml
├── src
│ └── test
│ ├── java
│ │ └── automation
│ │ └── miPrimerSimulacion.java
│ │
│ └── resources
│ └── Driver
│ └── chromedriver


---

## Ejecución de las pruebas

Para ejecutar el proyecto:

1. Clonar el repositorio.
2. Abrir el proyecto en **Eclipse** como proyecto Maven.
3. Verificar que las dependencias de Selenium se descarguen correctamente.
4. Configurar el navegador **Google Chrome** y el **ChromeDriver** correspondiente.
5. Ejecutar la clase de pruebas mediante **JUnit**.

---

## Notas sobre la simulación

- Las pruebas incluyen pausas controladas para facilitar la visualización del flujo durante la demostración.
- Algunos casos de prueba pueden generar resultados **fallidos o bloqueados** debido a condiciones del sistema bajo prueba (por ejemplo, usuarios previamente registrados o validaciones del sistema).
- Estos resultados forman parte del análisis de calidad del software.

---

## Autor

**María Paula Bermúdez**

Proyecto académico – Curso de **Calidad de Software**
