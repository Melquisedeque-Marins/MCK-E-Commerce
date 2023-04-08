INSERT INTO tb_category (name) VALUES ('telemóveis');
INSERT INTO tb_category (name) VALUES ('informática');
INSERT INTO tb_category (name) VALUES ('eletrodomésticos');
INSERT INTO tb_category (name) VALUES ('móveis');
INSERT INTO tb_category (name) VALUES ('tv');

INSERT INTO tb_product (name, description, price, sku_code, img_url, cover_img, rate, qty_reviews)  VALUES ('Desktop', 'PC Acer 8Gb de RAM, Processador intel i7, Placa de vídeo GForce 7600 e monitor LED 19', 3199.99, 'PC8i7M19B', 'https://imgs.casasbahia.com.br/1538322525/1xg.jpg?imwidth=500', 'https://imgs.casasbahia.com.br/1538322525/1xg.jpg?imwidth=500', 0.0, 0);
INSERT INTO tb_product (name, description, price, sku_code, img_url, cover_img, rate, qty_reviews)  VALUES ('notebook', 'Notebook DELL 16Gb de RAM, Processador intel i9, Placa de vídeo GForce 7600 e monitor LED 19', 5999.99, 'NB16i9M19B', 'https://i02.appmifile.com/109_operator_sg/23/08/2021/45d16ff368ca5f24dfa8f66d9238cb71.png', 'https://i02.appmifile.com/109_operator_sg/23/08/2021/45d16ff368ca5f24dfa8f66d9238cb71.png', 0.0, 0);
INSERT INTO tb_product (name, description, price, sku_code, img_url, cover_img, rate, qty_reviews)  VALUES ('Smart TV', 'Smart TV Sansung, LED, 70, Bivolt"', 4299.99, 'TVSL70B', 'https://v9y9v6a3.rocketcdn.me/wp-content/uploads/2021/05/kiboTEK_xiaomi_mi_tv_p1_32_006.png', 'https://v9y9v6a3.rocketcdn.me/wp-content/uploads/2021/05/kiboTEK_xiaomi_mi_tv_p1_32_006.png',  0.0, 0);
INSERT INTO tb_product (name, description, price, sku_code, img_url, cover_img, rate, qty_reviews)  VALUES ('Smartphone', 'Smartphone Motorola M159, 5G, tela 7", 6Gb de RAM, 256GB de aramazenamento interno', 2200.99, 'SPM6GB256', 'https://cdn.weasy.io/users/xiaomi/catalog/redmi_note_12_pro_5g_black.png', 'https://cdn.weasy.io/users/xiaomi/catalog/redmi_note_12_pro_5g_black.png', 0.0, 0);
INSERT INTO tb_product (name, description, price, sku_code, img_url, cover_img, rate, qty_reviews)  VALUES ('Smartphone S8', 'Smartphone Motorola M159, 5G, tela 7", 6Gb de RAM, 256GB de aramazenamento interno', 2200.99, 'SPM6GB256', 'https://cdn.weasy.io/users/xiaomi/catalog/redmi_note_12_pro_5g_black.png', 'https://cdn.weasy.io/users/xiaomi/catalog/redmi_note_12_pro_5g_black.png', 0.0, 0);

INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 4);
INSERT INTO tb_product_category (product_id, category_id) VALUES (5, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 2);
