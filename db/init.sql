INSERT INTO brand (name, creation_year, contact_phone, contact_email, description, address, country_code, logo_src) VALUES
    ('Toyota', 1937, '+81-3-3817-7111', 'contact@toyota.com', 'Японский автопроизводитель, один из крупнейших в мире', '1 Toyota-cho, Toyota City, Aichi, Japan', 'JPN', '/logos/toyota.png'),
    ('BMW', 1916, '+49-89-382-0', 'info@bmw.com', 'Немецкий производитель автомобилей и мотоциклов премиум-класса', 'Petuelring 130, 80809 Munich, Germany', 'DEU', '/logos/bmw.png'),
    ('Hyundai', 1967, '+82-2-3464-1114', 'global@hyundai.com', 'Южнокорейский автомобильный бренд', '12 Heolleung-ro, Seocho-gu, Seoul, South Korea', 'KOR', '/logos/hyundai.png'),
    ('Ford', 1903, '+1-800-392-3673', 'frc@ford.com', 'Американская автомобилестроительная компания', '1 American Road, Dearborn, MI 48126, USA', 'USA', '/logos/ford.png'),
    ('Mercedes-Benz', 1926, '+49-711-17-0', 'dialog@mercedes-benz.com', 'Немецкий производитель легковых автомобилей премиального класса', 'Mercedesstraße 120, 70372 Stuttgart, Germany', 'DEU', '/logos/mercedes.png');

INSERT INTO model (brand_id, name, year, min_cost, image_src) VALUES
    (1, 'Camry', 2024, 27000, '/images/camry.png'),
    (1, 'RAV4', 2024, 29000, '/images/rav4.png'),
    (2, 'X5', 2024, 62000, '/images/x5.png'),
    (3, 'Solaris', 2024, 18000, '/images/solaris.png'),
    (4, 'Focus', 2024, 22000, '/images/focus.png'),
    (5, 'E-Class', 2024, 55000, '/images/eclass.png');

INSERT INTO car_configuration (
    model_id, name, engine_type, engine_power, engine_volume, 
    fuel_consumption, fuel_type, tank_capacity, has_cruise_control, 
    basic_cost, seats_number, doors_count, transmission_type, 
    drive_type, is_basic, is_sales_stopped, additional_properties
) VALUES
    (1, 'Comfort', 'Petrol', 150, 2.5, 7.8, 'Petrol', 60, TRUE, 27000, 5, 4, 'Automatic', 'Front', TRUE, FALSE, '{"color_options": ["white", "black", "silver"]}'),
    (1, 'Luxury', 'Petrol', 200, 3.5, 9.2, 'Petrol', 60, TRUE, 35000, 5, 4, 'Automatic', 'Front', FALSE, FALSE, '{"color_options": ["black", "blue"], "leather": true}'),
    (2, 'Diesel', 'Diesel', 190, 2.0, 6.5, 'Diesel', 55, TRUE, 42000, 5, 5, 'Automatic', 'All-wheel', TRUE, FALSE, '{"roof_rails": true}'),
    (3, 'Base', 'Petrol', 123, 1.6, 6.9, 'Petrol', 50, FALSE, 18000, 5, 4, 'Manual', 'Front', TRUE, FALSE, '{}'),
    (4, 'Sport', 'Petrol', 250, 2.0, 8.5, 'Petrol', 52, TRUE, 28000, 5, 5, 'Automatic', 'Front', TRUE, FALSE, '{"sport_package": true}'),
    (5, 'Premium', 'Petrol', 367, 3.0, 8.2, 'Petrol', 66, TRUE, 65000, 5, 4, 'Automatic', 'Rear', TRUE, FALSE, '{"ambient_light": true, "premium_sound": true}');

INSERT INTO car (
    configuration_id, VIN, cost, color, mileage, last_lto_date,
    interior_color, seat_upholstery, is_new, additional_properties,
    image_src, is_test_drive_available, year, status
) VALUES
    (1, 'JTDBE32K123456789', 28000, 'Silver', 1500, '2024-01-15', 'Black', 'Fabric', FALSE, '{"has_navigation": true}', '/cars/camry1.jpg', TRUE, 2023, 'available'),
    (1, 'JTDBE32K987654321', 27500, 'White', 500, NULL, 'Beige', 'Fabric', TRUE, '{}', '/cars/camry2.jpg', TRUE, 2024, 'available'),
    (2, 'WBAJC110X12345678', 65000, 'Black', 100, NULL, 'Black', 'Leather', TRUE, '{"panorama": true}', '/cars/bmwx5.jpg', TRUE, 2024, 'available'),
    (3, 'KMHC541C123456789', 19000, 'Blue', 20000, '2023-12-10', 'Gray', 'Fabric', FALSE, '{}', '/cars/solaris.jpg', FALSE, 2022, 'on_test_drive'),
    (4, 'WF0HXXGAK12345678', 26000, 'Red', 50, NULL, 'Black', 'Fabric', TRUE, '{"sport_wheels": true}', '/cars/focus.jpg', TRUE, 2024, 'ordered'),
    (5, 'WDDZF4KB5PA123456', 68000, 'Silver', 10, NULL, 'Brown', 'Leather', TRUE, '{"night_package": true}', '/cars/eclass.jpg', TRUE, 2024, 'in_delivery'),
    (2, 'WBAJC110X87654321', 63000, 'White', 5000, '2024-01-20', 'Beige', 'Leather', FALSE, '{}', '/cars/bmwx5_2.jpg', TRUE, 2023, 'sold');

INSERT INTO client (name, email, address, phone, password_hash, registration_date) VALUES
    ('Иван Петров', 'ivan.petrov@email.com', 'ул. Ленина, 10, кв. 5, Москва', '+7(999)123-45-67', '$2y$10$YourHashHere1', '2024-01-10'),
    ('Мария Сидорова', 'maria.s@email.com', 'пр. Мира, 25, кв. 12, Санкт-Петербург', '+7(999)234-56-78', '$2y$10$YourHashHere2', '2024-01-15'),
    ('Алексей Иванов', 'alex.ivanov@email.com', 'ул. Советская, 5, кв. 3, Новосибирск', '+7(999)345-67-89', '$2y$10$YourHashHere3', '2024-01-20'),
    ('Елена Козлова', 'elena.k@email.com', 'ул. Пушкина, 15, кв. 8, Екатеринбург', '+7(999)456-78-90', '$2y$10$YourHashHere4', '2024-02-01'),
    ('Дмитрий Смирнов', 'dmitry.s@email.com', 'пр. Победы, 30, кв. 17, Казань', '+7(999)567-89-01', '$2y$10$YourHashHere5', '2024-02-05');

INSERT INTO manager (name, email, phone, password_hash) VALUES
    ('Анна Менеджер', 'anna.manager@dealership.com', '+7(999)111-22-33', '$2y$10$ManagerHash1'),
    ('Павел Продавец', 'pavel.seller@dealership.com', '+7(999)222-33-44', '$2y$10$ManagerHash2'),
    ('Ольга Консультант', 'olga.consultant@dealership.com', '+7(999)333-44-55', '$2y$10$ManagerHash3'),
    ('Сергей Старший', 'sergey.senior@dealership.com', '+7(999)444-55-66', '$2y$10$ManagerHash4'),
    ('Наталья Помощник', 'natalia.assist@dealership.com', '+7(999)555-66-77', '$2y$10$ManagerHash5');

INSERT INTO "order" (car_id, client_id, manager_id, order_date, test_drive_duration, test_drive_date, delivery_date, delivery_address, status) VALUES
    (1, 1, 1, '2024-02-10', NULL, NULL, '2024-02-20', 'ул. Ленина, 10, кв. 5, Москва', 'completed'),
    (3, 2, 2, '2024-02-15', NULL, NULL, '2024-02-28', 'пр. Мира, 25, кв. 12, Санкт-Петербург', 'in_delivery'),
    (5, 3, 3, '2024-02-20', 30, '2024-02-25 14:00:00', NULL, NULL, 'in_process'),
    (4, 4, 4, '2024-02-22', 45, '2024-02-26 11:00:00', NULL, NULL, 'in_process'),
    (2, 5, 5, '2024-02-23', NULL, NULL, '2024-03-05', 'пр. Победы, 30, кв. 17, Казань', 'in_delivery'),
    (7, 2, 1, '2024-01-25', NULL, NULL, '2024-02-01', 'пр. Мира, 25, кв. 12, Санкт-Петербург', 'completed'),
    (6, 4, 3, '2024-02-24', 60, '2024-02-28 16:00:00', '2024-03-10', 'ул. Пушкина, 15, кв. 8, Екатеринбург', 'in_delivery');
