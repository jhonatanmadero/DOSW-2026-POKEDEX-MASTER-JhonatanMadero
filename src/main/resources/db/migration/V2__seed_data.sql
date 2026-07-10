-- ============================================================
-- V2: Datos semilla - Regiones, habilidades y subconjunto
-- representativo de Pokemon (Generaciones 1-3) para la demo.
-- Nota: subconjunto elegido para cubrir todos los filtros del
-- RF-07 a RF-14 y RF-22 (varias regiones, tipos, generaciones,
-- rangos de stats, habilidades, mega evolucion y evoluciones).
-- ============================================================

INSERT INTO region (name, sort_order) VALUES
('Kanto', 1), ('Johto', 2), ('Hoenn', 3), ('Sinnoh', 4),
('Unova', 5), ('Kalos', 6), ('Alola', 7), ('Galar', 8), ('Paldea', 9);

INSERT INTO ability (name, description, is_hidden) VALUES
('Espesura', 'Potencia los movimientos de tipo Planta cuando los PS son bajos.', FALSE),
('Mar Llamas', 'Potencia los movimientos de tipo Fuego cuando los PS son bajos.', FALSE),
('Torrente', 'Potencia los movimientos de tipo Agua cuando los PS son bajos.', FALSE),
('Electricidad Estatica', 'Puede paralizar al rival por contacto.', FALSE),
('Pararrayos', 'Atrae los movimientos electricos del rival hacia si mismo.', TRUE),
('Cuerpo Puro', 'Evita que otros Pokemon bajen sus estadisticas.', FALSE),
('Levitacion', 'Otorga inmunidad a movimientos de tipo Tierra.', FALSE),
('Roca Solida', 'Reduce el dano de golpes super efectivos.', FALSE),
('Foco Interno', 'Evita el retroceso y la intimidacion.', TRUE),
('Presion', 'Aumenta los PP que gasta el rival al atacar.', FALSE),
('Guardia Mistica', 'Reduce el dano de ataques multiples.', TRUE),
('Adaptable', 'Aumenta el bono de mismo tipo (STAB).', FALSE);

-- ================= POKEMON =================
-- Kanto (Gen 1)
INSERT INTO pokemon (national_number, name, description, image_url, type_primary, type_secondary, region_id, generation, height_meters, weight_kg, has_mega, evolution_stage, evolution_level, evolution_method)
VALUES
(1, 'Bulbasaur', 'Pokemon Semilla. Tiene una semilla en el lomo desde que nace.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png', 'Planta', 'Veneno', (SELECT id FROM region WHERE name='Kanto'), 1, 0.7, 6.9, FALSE, 'BASICO', 16, 'Nivel'),
(2, 'Ivysaur', 'Pokemon Semilla. La semilla en su lomo crecio hasta ser una flor.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png', 'Planta', 'Veneno', (SELECT id FROM region WHERE name='Kanto'), 1, 1.0, 13.0, FALSE, 'PRIMERA', 32, 'Nivel'),
(3, 'Venusaur', 'Pokemon Semilla. Su flor libera un aroma relajante.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png', 'Planta', 'Veneno', (SELECT id FROM region WHERE name='Kanto'), 1, 2.0, 100.0, TRUE, 'SEGUNDA', NULL, NULL),
(4, 'Charmander', 'Pokemon Lagartija. La llama de su cola indica su estado de animo.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png', 'Fuego', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.6, 8.5, FALSE, 'BASICO', 16, 'Nivel'),
(5, 'Charmeleon', 'Pokemon Llama. Cuando se enoja su llama arde con mas fuerza.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png', 'Fuego', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 1.1, 19.0, FALSE, 'PRIMERA', 36, 'Nivel'),
(6, 'Charizard', 'Pokemon Llama. Escupe fuego lo suficientemente caliente como para fundir rocas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png', 'Fuego', 'Volador', (SELECT id FROM region WHERE name='Kanto'), 1, 1.7, 90.5, TRUE, 'SEGUNDA', NULL, NULL),
(7, 'Squirtle', 'Pokemon Tortuga. Se retrae en su caparazon para defenderse.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.5, 9.0, FALSE, 'BASICO', 16, 'Nivel'),
(8, 'Wartortle', 'Pokemon Tortuga. Su cola peluda es simbolo de longevidad.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/8.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 1.0, 22.5, FALSE, 'PRIMERA', 36, 'Nivel'),
(9, 'Blastoise', 'Pokemon Concha. Dispara agua a presion desde los canones de su caparazon.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 1.6, 85.5, TRUE, 'SEGUNDA', NULL, NULL),
(25, 'Pikachu', 'Pokemon Raton. Guarda electricidad en sus mejillas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png', 'Electrico', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.4, 6.0, FALSE, 'PRIMERA', NULL, 'Piedra Trueno'),
(26, 'Raichu', 'Pokemon Raton. Sus largas colas actuan como pararrayos.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/26.png', 'Electrico', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.8, 30.0, FALSE, 'SEGUNDA', NULL, NULL),
(39, 'Jigglypuff', 'Pokemon Globo. Canta una nana que hace dormir a quien la escucha.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/39.png', 'Normal', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.5, 5.5, FALSE, 'BASICO', NULL, 'Piedra Lunar'),
(52, 'Meowth', 'Pokemon Aranador. Adora las cosas brillantes y redondas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/52.png', 'Normal', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.4, 4.2, FALSE, 'BASICO', 28, 'Nivel'),
(54, 'Psyduck', 'Pokemon Pato. Sufre constantes dolores de cabeza.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/54.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.8, 19.6, FALSE, 'BASICO', 33, 'Nivel'),
(66, 'Machop', 'Pokemon Superpoder. Entrena su cuerpo musculoso a diario.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/66.png', 'Lucha', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.8, 19.5, FALSE, 'BASICO', 28, 'Nivel'),
(94, 'Gengar', 'Pokemon Sombra. Se dice que aparece si sientes un escalofrio repentino.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/94.png', 'Fantasma', 'Veneno', (SELECT id FROM region WHERE name='Kanto'), 1, 1.5, 40.5, TRUE, 'SEGUNDA', NULL, NULL),
(95, 'Onix', 'Pokemon Serpiente Roca. Crece cavando a gran velocidad bajo tierra.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/95.png', 'Roca', 'Tierra', (SELECT id FROM region WHERE name='Kanto'), 1, 8.8, 210.0, FALSE, 'BASICO', NULL, 'Intercambio'),
(133, 'Eevee', 'Pokemon Evolucion. Tiene un ADN inestable que le permite evolucionar de muchas formas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png', 'Normal', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.3, 6.5, FALSE, 'BASICO', NULL, 'Piedra'),
(134, 'Vaporeon', 'Pokemon Burbuja. Su estructura celular es similar a la del agua.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/134.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 1.0, 29.0, FALSE, 'PRIMERA', NULL, NULL),
(135, 'Jolteon', 'Pokemon Relampago. Almacena electricidad estatica en su pelaje.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/135.png', 'Electrico', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.8, 24.5, FALSE, 'PRIMERA', NULL, NULL),
(136, 'Flareon', 'Pokemon Llama. Su bolsa de fuego interna alcanza los 900 grados.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/136.png', 'Fuego', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.9, 25.0, FALSE, 'PRIMERA', NULL, NULL),
(143, 'Snorlax', 'Pokemon Dormilon. Come casi 400kg de comida al dia y luego duerme.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png', 'Normal', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 2.1, 460.0, FALSE, 'SIN_EVOLUCION', NULL, NULL),
(149, 'Dragonite', 'Pokemon Dragon. Se dice que puede volar alrededor del mundo en 16 horas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/149.png', 'Dragon', 'Volador', (SELECT id FROM region WHERE name='Kanto'), 1, 2.2, 210.0, FALSE, 'SEGUNDA', NULL, NULL),
(150, 'Mewtwo', 'Pokemon Genetico. Fue creado mediante manipulacion genetica.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png', 'Psiquico', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 2.0, 122.0, TRUE, 'LEGENDARIO', NULL, NULL),
(151, 'Mew', 'Pokemon Nuevo Especie. Se dice que posee el codigo genetico de todos los Pokemon.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/151.png', 'Psiquico', NULL, (SELECT id FROM region WHERE name='Kanto'), 1, 0.4, 4.0, FALSE, 'LEGENDARIO', NULL, NULL);

-- Johto (Gen 2)
INSERT INTO pokemon (national_number, name, description, image_url, type_primary, type_secondary, region_id, generation, height_meters, weight_kg, has_mega, evolution_stage, evolution_level, evolution_method)
VALUES
(152, 'Chikorita', 'Pokemon Hoja. La hoja de su cabeza detecta la humedad del aire.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/152.png', 'Planta', NULL, (SELECT id FROM region WHERE name='Johto'), 2, 0.9, 6.4, FALSE, 'BASICO', 16, 'Nivel'),
(155, 'Cyndaquil', 'Pokemon Raton de Fuego. Llamas brotan de su lomo cuando se asusta.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/155.png', 'Fuego', NULL, (SELECT id FROM region WHERE name='Johto'), 2, 0.5, 7.9, FALSE, 'BASICO', 14, 'Nivel'),
(158, 'Totodile', 'Pokemon Cocodrilo Grande. Muerde todo lo que tiene delante con sus fuertes mandibulas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/158.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Johto'), 2, 0.6, 9.5, FALSE, 'BASICO', 18, 'Nivel'),
(249, 'Lugia', 'Pokemon Buceo. Se dice que puede provocar tormentas de 40 dias con solo batir sus alas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/249.png', 'Psiquico', 'Volador', (SELECT id FROM region WHERE name='Johto'), 2, 5.2, 216.0, FALSE, 'LEGENDARIO', NULL, NULL);

-- Hoenn (Gen 3)
INSERT INTO pokemon (national_number, name, description, image_url, type_primary, type_secondary, region_id, generation, height_meters, weight_kg, has_mega, evolution_stage, evolution_level, evolution_method)
VALUES
(252, 'Treecko', 'Pokemon Geco. Las almohadillas de sus pies tienen microganchos invisibles.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/252.png', 'Planta', NULL, (SELECT id FROM region WHERE name='Hoenn'), 3, 0.5, 5.0, FALSE, 'BASICO', 16, 'Nivel'),
(255, 'Torchic', 'Pokemon Polluelo. Tiene una bolsa de fuego interna que siempre esta encendida.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/255.png', 'Fuego', NULL, (SELECT id FROM region WHERE name='Hoenn'), 3, 0.4, 2.5, FALSE, 'BASICO', 16, 'Nivel'),
(258, 'Mudkip', 'Pokemon Pez Barro. La aleta de su cabeza detecta cambios en el aire y el agua.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/258.png', 'Agua', NULL, (SELECT id FROM region WHERE name='Hoenn'), 3, 0.4, 7.6, FALSE, 'BASICO', 16, 'Nivel'),
(384, 'Rayquaza', 'Pokemon Cielo. Vivio durante miles de anios en la capa de ozono.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/384.png', 'Dragon', 'Volador', (SELECT id FROM region WHERE name='Hoenn'), 3, 7.0, 206.5, TRUE, 'LEGENDARIO', NULL, NULL);

-- ================= EVOLUCIONES (auto-referencia) =================
UPDATE pokemon SET evolves_to_id = (SELECT id FROM pokemon WHERE national_number=2) WHERE national_number=1;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=1), evolves_to_id = (SELECT id FROM pokemon WHERE national_number=3) WHERE national_number=2;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=2) WHERE national_number=3;

UPDATE pokemon SET evolves_to_id = (SELECT id FROM pokemon WHERE national_number=5) WHERE national_number=4;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=4), evolves_to_id = (SELECT id FROM pokemon WHERE national_number=6) WHERE national_number=5;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=5) WHERE national_number=6;

UPDATE pokemon SET evolves_to_id = (SELECT id FROM pokemon WHERE national_number=8) WHERE national_number=7;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=7), evolves_to_id = (SELECT id FROM pokemon WHERE national_number=9) WHERE national_number=8;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=8) WHERE national_number=9;

UPDATE pokemon SET evolves_to_id = (SELECT id FROM pokemon WHERE national_number=26) WHERE national_number=25;
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=25) WHERE national_number=26;

UPDATE pokemon SET evolves_to_id = (SELECT id FROM pokemon WHERE national_number=134) WHERE national_number=133; -- eevee -> vaporeon (rama principal para la demo)
UPDATE pokemon SET evolves_from_id = (SELECT id FROM pokemon WHERE national_number=133) WHERE national_number IN (134,135,136);

-- ================= ESTADISTICAS BASE =================
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) VALUES
((SELECT id FROM pokemon WHERE national_number=1), 45,49,49,65,65,45),
((SELECT id FROM pokemon WHERE national_number=2), 60,62,63,80,80,60),
((SELECT id FROM pokemon WHERE national_number=3), 80,82,83,100,100,80),
((SELECT id FROM pokemon WHERE national_number=4), 39,52,43,60,50,65),
((SELECT id FROM pokemon WHERE national_number=5), 58,64,58,80,65,80),
((SELECT id FROM pokemon WHERE national_number=6), 78,84,78,109,85,100),
((SELECT id FROM pokemon WHERE national_number=7), 44,48,65,50,64,43),
((SELECT id FROM pokemon WHERE national_number=8), 59,63,80,65,80,58),
((SELECT id FROM pokemon WHERE national_number=9), 79,83,100,85,105,78),
((SELECT id FROM pokemon WHERE national_number=25), 35,55,40,50,50,90),
((SELECT id FROM pokemon WHERE national_number=26), 60,90,55,90,80,110),
((SELECT id FROM pokemon WHERE national_number=39), 115,45,20,45,25,20),
((SELECT id FROM pokemon WHERE national_number=52), 40,45,35,40,40,90),
((SELECT id FROM pokemon WHERE national_number=54), 50,52,48,65,50,55),
((SELECT id FROM pokemon WHERE national_number=66), 70,80,50,35,35,35),
((SELECT id FROM pokemon WHERE national_number=94), 60,65,60,130,75,110),
((SELECT id FROM pokemon WHERE national_number=95), 35,45,160,30,45,70),
((SELECT id FROM pokemon WHERE national_number=133), 55,55,50,45,65,55),
((SELECT id FROM pokemon WHERE national_number=134), 130,65,60,110,95,65),
((SELECT id FROM pokemon WHERE national_number=135), 65,65,60,110,95,130),
((SELECT id FROM pokemon WHERE national_number=136), 65,130,60,95,110,65),
((SELECT id FROM pokemon WHERE national_number=143), 160,110,65,65,110,30),
((SELECT id FROM pokemon WHERE national_number=149), 91,134,95,100,100,80),
((SELECT id FROM pokemon WHERE national_number=150), 106,110,90,154,90,130),
((SELECT id FROM pokemon WHERE national_number=151), 100,100,100,100,100,100),
((SELECT id FROM pokemon WHERE national_number=152), 45,49,65,49,65,45),
((SELECT id FROM pokemon WHERE national_number=155), 39,52,43,60,50,65),
((SELECT id FROM pokemon WHERE national_number=158), 50,65,64,44,48,43),
((SELECT id FROM pokemon WHERE national_number=249), 106,90,130,90,154,110),
((SELECT id FROM pokemon WHERE national_number=252), 40,45,35,65,55,70),
((SELECT id FROM pokemon WHERE national_number=255), 45,60,40,70,50,45),
((SELECT id FROM pokemon WHERE national_number=258), 50,70,50,50,50,40),
((SELECT id FROM pokemon WHERE national_number=384), 105,150,90,150,90,95);

-- ================= HABILIDADES POR POKEMON =================
INSERT INTO pokemon_ability (pokemon_id, ability_id) VALUES
((SELECT id FROM pokemon WHERE national_number=1), (SELECT id FROM ability WHERE name='Espesura')),
((SELECT id FROM pokemon WHERE national_number=2), (SELECT id FROM ability WHERE name='Espesura')),
((SELECT id FROM pokemon WHERE national_number=3), (SELECT id FROM ability WHERE name='Espesura')),
((SELECT id FROM pokemon WHERE national_number=4), (SELECT id FROM ability WHERE name='Mar Llamas')),
((SELECT id FROM pokemon WHERE national_number=5), (SELECT id FROM ability WHERE name='Mar Llamas')),
((SELECT id FROM pokemon WHERE national_number=6), (SELECT id FROM ability WHERE name='Mar Llamas')),
((SELECT id FROM pokemon WHERE national_number=7), (SELECT id FROM ability WHERE name='Torrente')),
((SELECT id FROM pokemon WHERE national_number=8), (SELECT id FROM ability WHERE name='Torrente')),
((SELECT id FROM pokemon WHERE national_number=9), (SELECT id FROM ability WHERE name='Torrente')),
((SELECT id FROM pokemon WHERE national_number=25), (SELECT id FROM ability WHERE name='Electricidad Estatica')),
((SELECT id FROM pokemon WHERE national_number=26), (SELECT id FROM ability WHERE name='Electricidad Estatica')),
((SELECT id FROM pokemon WHERE national_number=26), (SELECT id FROM ability WHERE name='Pararrayos')),
((SELECT id FROM pokemon WHERE national_number=94), (SELECT id FROM ability WHERE name='Levitacion')),
((SELECT id FROM pokemon WHERE national_number=95), (SELECT id FROM ability WHERE name='Roca Solida')),
((SELECT id FROM pokemon WHERE national_number=143), (SELECT id FROM ability WHERE name='Foco Interno')),
((SELECT id FROM pokemon WHERE national_number=149), (SELECT id FROM ability WHERE name='Foco Interno')),
((SELECT id FROM pokemon WHERE national_number=150), (SELECT id FROM ability WHERE name='Presion')),
((SELECT id FROM pokemon WHERE national_number=151), (SELECT id FROM ability WHERE name='Cuerpo Puro')),
((SELECT id FROM pokemon WHERE national_number=249), (SELECT id FROM ability WHERE name='Presion')),
((SELECT id FROM pokemon WHERE national_number=384), (SELECT id FROM ability WHERE name='Adaptable'));

-- ================= USUARIO ADMIN SEMILLA =================
-- Contrasena en texto plano: Admin123 (hash BCrypt real, verificado)
INSERT INTO app_user (full_name, email, password_hash, role, active, from_google)
VALUES ('Administrador Pokedex', 'admin@pokedex.com', '$2b$10$GbINQMIeOplrch6FRfORbOkR0fu/9WonqkMp5dmK7TGpzS0eR/iYi', 'ADMIN', TRUE, FALSE);
