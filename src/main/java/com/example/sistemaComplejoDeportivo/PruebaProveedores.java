package com.example.sistemaComplejoDeportivo;

import com.example.sistemaComplejoDeportivo.model.Proveedor;
import com.example.sistemaComplejoDeportivo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")  // Solo se ejecuta cuando el perfil "dev" esté activo
public class PruebaProveedores implements CommandLineRunner {

    @Autowired
    private ProveedorService proveedorService;

    @Override
    public void run(String... args) {
        System.out.println("🔍 Iniciando prueba de proveedores...");

        // Obtener lista de proveedores
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();

        // Imprimir resultados en consola
        System.out.println("✅ Número de proveedores encontrados: " + proveedores.size());
        for (Proveedor p : proveedores) {
            System.out.println("📌 ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombreEmpresa());
        }

    }
}
