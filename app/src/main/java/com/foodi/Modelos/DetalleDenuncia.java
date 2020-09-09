package com.foodi.Modelos;

public class DetalleDenuncia {

    private Denuncia idDenuncia;
    private String idDetalleDenuncia;
    private byte[] imagen;

    public class Denuncia{
        private String detalles, fecha, idDenuncia, latitud, longitud, tipo, titulo;
        private Usuario idUsuario;

        public Denuncia(String detalles, String fecha, String idDenuncia, String latitud, String longitud, String tipo, String titulo, Usuario idUsuario) {
            this.detalles = detalles;
            this.fecha = fecha;
            this.idDenuncia = idDenuncia;
            this.latitud = latitud;
            this.longitud = longitud;
            this.tipo = tipo;
            this.titulo = titulo;
            this.idUsuario = idUsuario;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getIdDenuncia() {
            return idDenuncia;
        }

        public void setIdDenuncia(String idDenuncia) {
            this.idDenuncia = idDenuncia;
        }

        public String getLatitud() {
            return latitud;
        }

        public void setLatitud(String latitud) {
            this.latitud = latitud;
        }

        public String getLongitud() {
            return longitud;
        }

        public void setLongitud(String longitud) {
            this.longitud = longitud;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public Usuario getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(Usuario idUsuario) {
            this.idUsuario = idUsuario;
        }

        public class Usuario{
            private String nombres, apellidos, telefono, latitudActual, longitudActual, nombreUsuario, tipo, idUsuario;
            private byte[] imagen;

            public Usuario(String nombres, String apellidos, String telefono, String latitudActual, String longitudActual, String nombreUsuario, String tipo, String idUsuario, byte[] imagen) {
                this.nombres = nombres;
                this.apellidos = apellidos;
                this.telefono = telefono;
                this.latitudActual = latitudActual;
                this.longitudActual = longitudActual;
                this.nombreUsuario = nombreUsuario;
                this.tipo = tipo;
                this.idUsuario = idUsuario;
                this.imagen = imagen;
            }

            public String getNombres() {
                return nombres;
            }

            public void setNombres(String nombres) {
                this.nombres = nombres;
            }

            public String getApellidos() {
                return apellidos;
            }

            public void setApellidos(String apellidos) {
                this.apellidos = apellidos;
            }

            public String getTelefono() {
                return telefono;
            }

            public void setTelefono(String telefono) {
                this.telefono = telefono;
            }

            public String getLatitudActual() {
                return latitudActual;
            }

            public void setLatitudActual(String latitudActual) {
                this.latitudActual = latitudActual;
            }

            public String getLongitudActual() {
                return longitudActual;
            }

            public void setLongitudActual(String longitudActual) {
                this.longitudActual = longitudActual;
            }

            public String getNombreUsuario() {
                return nombreUsuario;
            }

            public void setNombreUsuario(String nombreUsuario) {
                this.nombreUsuario = nombreUsuario;
            }

            public String getTipo() {
                return tipo;
            }

            public void setTipo(String tipo) {
                this.tipo = tipo;
            }

            public String getIdUsuario() {
                return idUsuario;
            }

            public void setIdUsuario(String idUsuario) {
                this.idUsuario = idUsuario;
            }

            public byte[] getImagen() {
                return imagen;
            }

            public void setImagen(byte[] imagen) {
                this.imagen = imagen;
            }
        }


    }


    public DetalleDenuncia(Denuncia idDenuncia, String idDetalleDenuncia, byte[] imagen) {
        this.idDenuncia = idDenuncia;
        this.idDetalleDenuncia = idDetalleDenuncia;
        this.imagen = imagen;
    }

    public Denuncia getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(Denuncia idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public String getIdDetalleDenuncia() {
        return idDetalleDenuncia;
    }

    public void setIdDetalleDenuncia(String idDetalleDenuncia) {
        this.idDetalleDenuncia = idDetalleDenuncia;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
