
-- Insert mock user for development
INSERT INTO users (name, email) VALUES ('Developer', 'dev@example.com');

-- Insert initial list for the mock user
INSERT INTO listify_lists (user_id, list_name) VALUES (2, 'Min handleliste');

-- Insert initial items for the mock user's list with position
INSERT INTO list_items (name, state, position, listify_list_id) VALUES ('Batterier', FALSE, 1, (SELECT id FROM listify_lists WHERE list_name = 'Min handleliste'));
INSERT INTO list_items (name, state, position, listify_list_id) VALUES ('Fisk', TRUE, 2, (SELECT id FROM listify_lists WHERE list_name = 'Min handleliste'));
INSERT INTO list_items (name, state, position, listify_list_id) VALUES ('Pepsi', FALSE, 3, (SELECT id FROM listify_lists WHERE list_name = 'Min handleliste'));
INSERT INTO list_items (name, state, position, listify_list_id) VALUES ('Yoghurt x2', FALSE, 4, (SELECT id FROM listify_lists WHERE list_name = 'Min handleliste'));

-- Insert initial collaborator for the mock user's list (assuming user with id 2)
INSERT INTO list_collaborators (user_id, listify_list_id) VALUES (2, (SELECT id FROM listify_lists WHERE list_name = 'Min handleliste'));

