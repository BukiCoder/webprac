CREATE TYPE car_status AS ENUM (
    'on_test_drive',
    'in_delivery',
    'available',
    'unavailable',
    'on_lto',
    'ordered',
    'sold'
);

CREATE TYPE order_status AS ENUM (
    'completed',
    'canceled',
    'in_process',
    'in_delivery',
    'expired'
);

CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    creation_year INTEGER NOT NULL,
    contact_phone VARCHAR NOT NULL,
    contact_email VARCHAR NOT NULL,
    description TEXT,
    address VARCHAR,
    country_code CHAR(3) NOT NULL,
    logo_src VARCHAR NOT NULL,
    CONSTRAINT valid_creation_year CHECK (creation_year > 1885 AND creation_year <= EXTRACT(YEAR FROM CURRENT_DATE))
);

CREATE TABLE model (
    id SERIAL PRIMARY KEY,
    brand_id INTEGER NOT NULL REFERENCES brand(id) ON DELETE CASCADE,
    name VARCHAR NOT NULL,
    year INTEGER NOT NULL,
    min_cost INTEGER NOT NULL,
    image_src VARCHAR,
    CONSTRAINT positive_min_cost CHECK (min_cost > 0),
    CONSTRAINT valid_model_year CHECK (year > 1900 AND year <= EXTRACT(YEAR FROM CURRENT_DATE) + 1)
);

CREATE TABLE car_configuration (
    id SERIAL PRIMARY KEY,
    model_id INTEGER NOT NULL REFERENCES model(id) ON DELETE CASCADE,
    name VARCHAR NOT NULL,
    engine_type VARCHAR NOT NULL,
    engine_power INTEGER NOT NULL,
    engine_volume NUMERIC NOT NULL,
    fuel_consumption NUMERIC NOT NULL,
    fuel_type VARCHAR NOT NULL,
    tank_capacity NUMERIC NOT NULL,
    has_cruise_control BOOLEAN NOT NULL,
    basic_cost INTEGER NOT NULL,
    seats_number INTEGER NOT NULL,
    doors_count INTEGER NOT NULL,
    transmission_type VARCHAR NOT NULL,
    drive_type VARCHAR NOT NULL,
    is_basic BOOLEAN NOT NULL DEFAULT FALSE,
    is_sales_stopped BOOLEAN NOT NULL DEFAULT FALSE,
    additional_properties JSON,
    CONSTRAINT positive_engine_power CHECK (engine_power > 0),
    CONSTRAINT positive_engine_volume CHECK (engine_volume > 0),
    CONSTRAINT positive_fuel_consumption CHECK (fuel_consumption > 0),
    CONSTRAINT positive_tank_capacity CHECK (tank_capacity > 0),
    CONSTRAINT positive_basic_cost CHECK (basic_cost > 0),
    CONSTRAINT positive_seats CHECK (seats_number > 0),
    CONSTRAINT positive_doors CHECK (doors_count > 0)
);

CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    configuration_id INTEGER NOT NULL REFERENCES car_configuration(id) ON DELETE CASCADE,
    VIN VARCHAR NOT NULL UNIQUE,
    cost INTEGER NOT NULL,
    color VARCHAR NOT NULL,
    mileage INTEGER,
    last_lto_date DATE,
    interior_color VARCHAR NOT NULL,
    seat_upholstery VARCHAR,
    is_new BOOLEAN NOT NULL,
    additional_properties JSON,
    image_src VARCHAR,
    is_test_drive_available BOOLEAN NOT NULL DEFAULT TRUE,
    year INTEGER NOT NULL,
    status car_status NOT NULL DEFAULT 'available',
    CONSTRAINT positive_cost CHECK (cost > 0),
    CONSTRAINT positive_mileage CHECK (mileage >= 0),
    CONSTRAINT valid_year CHECK (year > 1900 AND year <= EXTRACT(YEAR FROM CURRENT_DATE) + 1)
);

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    address TEXT NOT NULL,
    phone VARCHAR NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL
);

CREATE TABLE manager (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    phone VARCHAR NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL
);

CREATE TABLE "order" (
    id SERIAL PRIMARY KEY,
    car_id INTEGER NOT NULL REFERENCES car(id) ON DELETE CASCADE,
    client_id INTEGER NOT NULL REFERENCES client(id) ON DELETE CASCADE,
    manager_id INTEGER NOT NULL REFERENCES manager(id) ON DELETE CASCADE,
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    test_drive_duration INTEGER,
    test_drive_date TIMESTAMP,
    delivery_date DATE,
    delivery_address VARCHAR,
    status order_status NOT NULL DEFAULT 'in_process',
    CONSTRAINT positive_duration CHECK (test_drive_duration > 0),
    CONSTRAINT valid_delivery_date CHECK (delivery_date IS NULL OR delivery_date >= order_date)
);
