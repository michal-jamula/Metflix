-- Gives each user an initial "Member" role
INSERT INTO user_authorities (user_id, authority_id)
SELECT id, 1
FROM users u
WHERE NOT EXISTS (
    SELECT 1
    FROM user_authorities a
    WHERE u.id = a.user_id AND authority_id = 1
);

INSERT INTO user_authorities(user_id, authority_id) VALUES (22, 2);
INSERT INTO user_authorities(user_id, authority_id) VALUES (22, 3);
INSERT INTO user_authorities(user_id, authority_id) VALUES (24, 2);


