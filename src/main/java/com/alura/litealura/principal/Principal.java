package com.alura.litealura.principal;

import com.alura.litealura.model.Autor;
import com.alura.litealura.model.DatosLibro;
import com.alura.litealura.model.Libro;
import com.alura.litealura.model.RespuestaAPI;
import com.alura.litealura.repository.AutorRepository;
import com.alura.litealura.repository.LibroRepository;
import com.alura.litealura.service.ConsumoAPI;
import com.alura.litealura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var tituloLibro = teclado.nextLine();
        
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(tituloLibro);
        if (libroExistente.isPresent()) {
            System.out.println("El libro ya está registrado en la base de datos.");
            return;
        }

        var json = consumoAPI.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "%20"));
        var datos = conversor.obtenerDatos(json, RespuestaAPI.class);
        
        Optional<DatosLibro> libroBuscado = datos.resultados().stream().findFirst();
        
        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();
            System.out.println("Libro encontrado: " + datosLibro.titulo());
            
            Autor autor = null;
            if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                var datosAutor = datosLibro.autores().get(0);
                Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());
                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();
                } else {
                    autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                }
            } else {
                System.out.println("No se encontró un autor para este libro en la API.");
            }
            
            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            libroRepository.save(libro);
            
            System.out.println("Libro guardado exitosamente!");
            System.out.println(libro);
        } else {
            System.out.println("Libro no encontrado en la API.");
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados aún.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados aún.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepository.buscarAutoresVivosEnAnio(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en ese año registrados.");
            } else {
                autores.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Debe ser un número.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        var idioma = teclado.nextLine();
        
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            libros.forEach(System.out::println);
        }
    }
}
