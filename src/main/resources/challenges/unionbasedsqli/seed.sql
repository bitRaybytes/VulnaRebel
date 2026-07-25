INSERT INTO products (name, category, price, description)
VALUES
    ('Mechanical Keyboard', 'Peripherals', 129,
     'RGB mechanical keyboard with blue switches'),

    ('Gaming Mouse', 'Peripherals', 69,
     'High precision optical gaming mouse'),

    ('USB-C Dock', 'Accessories', 149,
     'Docking station with HDMI and Ethernet'),

    ('27" Monitor', 'Displays', 329,
     '2560x1440 IPS monitor'),

    ('Laptop Stand', 'Accessories', 39,
     'Aluminium adjustable laptop stand');


INSERT INTO application_settings
(setting_key, setting_value)
VALUES
    ('version', '1.3.0'),
    ('theme', 'dark'),
    ('maintenance', 'false'),
    ('deployment_key', 'ctf{A_Union_SQL_Injector}');