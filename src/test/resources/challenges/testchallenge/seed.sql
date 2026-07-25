INSERT INTO test_table(id,name)
VALUES (1,'Alice') ON CONFLICT DO NOTHING;