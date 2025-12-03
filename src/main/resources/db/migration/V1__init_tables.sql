-- Tabla para almacenar los lugares (Venues)
CREATE TABLE venue (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100),
    capacity INT NOT NULL
);

-- Tabla para almacenar las categorías de los eventos
CREATE TABLE category (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla para almacenar los eventos (Events)
CREATE TABLE event (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    date DATE,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    venue_id VARCHAR(36),
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venue(id)
);

-- Tabla de unión para la relación ManyToMany entre Event y Category
CREATE TABLE event_category (
    event_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (event_id, category_id),
    CONSTRAINT fk_event_category_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_category_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);