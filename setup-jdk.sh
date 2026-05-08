#!/bin/bash

echo "═══════════════════════════════════════════════════════════════"
echo "Configurando JDK para el proyecto API-Bank"
echo "═══════════════════════════════════════════════════════════════"

PROJECT_DIR="/home/tony/JAVAProjects/Proyectos_SpringBoot/API-Bank"

cd "$PROJECT_DIR"

echo ""
echo "1️⃣  Limpiando caché de IntelliJ..."
rm -rf .idea
rm -rf *.iml
echo "✅ Caché limpiado"

echo ""
echo "2️⃣  Limpiando build de Maven..."
rm -rf target
echo "✅ Build limpiado"

echo ""
echo "3️⃣  Recreando configuración de Maven..."
mvn clean
echo "✅ Maven limpio"

echo ""
echo "4️⃣  Descargando dependencias..."
mvn dependency:resolve
echo "✅ Dependencias descargadas"

echo ""
echo "5️⃣  Compilando proyecto..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa: BUILD SUCCESS"
else
    echo "❌ Error en compilación"
    exit 1
fi

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "✅ CONFIGURACIÓN COMPLETADA"
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "Próximos pasos:"
echo "1. Abre el proyecto en IntelliJ"
echo "2. File → Project Structure → Project → SDK: Java 21"
echo "3. Click Apply y OK"
echo "4. Espera a que indexe"
echo ""
echo "Luego puedes ejecutar:"
echo "  $ ./mvnw spring-boot:run"
echo ""

