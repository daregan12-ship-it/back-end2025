package mx.tecnm.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import mx.tecnm.backend.api.models.Producto;

@RestController
public class Test {
    @GetMapping("/hello")
    public String helloworld(){
        return "Hola API Rest";
    }

    @GetMapping("/producto")
    public Producto getProducto(){
        Producto producto = new Producto();
        producto.nombre = "Yoghurt Dannone Fresa";
        producto.codigoBarras = "";
        producto.precio = 12.00;
        return producto;
    }

    @GetMapping("/productos")
    public Producto[] getProductos(){
        Producto producto1 = new Producto();
        producto1.nombre = "Yoghurt Dannone Fresa";
        producto1.codigoBarras = "7501035801234";
        producto1.precio = 12.00;

        Producto producto2 = new Producto();
        producto2.nombre = "Leche Lala";
        producto2.codigoBarras = "7501035801235";
        producto2.precio = 10.00;

        return new Producto[]{producto1, producto2};
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable int id) {
        Producto p1 = new Producto();
        p1.nombre = "Manzana";
        p1.codigoBarras = "ABC123";
        p1.precio = 12.5;

        Producto p2 = new Producto();
        p2.nombre = "Plátano";
        p2.codigoBarras = "DEF456";
        p2.precio = 8.9;

        Producto p3 = new Producto();
        p3.nombre = "Naranja";
        p3.codigoBarras = "GHI789";
        p3.precio = 10.0;

        Producto[] productos = {p1, p2, p3};

        if (id < 0 || id >= productos.length) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productos[id]);
    }
}
