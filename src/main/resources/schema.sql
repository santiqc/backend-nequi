CREATE TABLE franquicias (
                             id SERIAL PRIMARY KEY,
                             nombre VARCHAR(255) NOT NULL
);

CREATE TABLE sucursales (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR(255) NOT NULL,
                            franquicia_id BIGINT REFERENCES franquicias(id)
);

CREATE TABLE productos (
                           id SERIAL PRIMARY KEY,
                           nombre VARCHAR(255) NOT NULL,
                           stock INTEGER NOT NULL,
                           sucursal_id BIGINT REFERENCES sucursales(id)
);