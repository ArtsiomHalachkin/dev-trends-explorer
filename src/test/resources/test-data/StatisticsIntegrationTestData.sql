
INSERT INTO languages (id, name) VALUES (1, 'Go');
INSERT INTO languages (id, name) VALUES (2, 'HTML');

INSERT INTO owners (id, login) VALUES (1, 'google');
INSERT INTO owners (id, login) VALUES (2, 'facebook');

INSERT INTO domains (id, name) VALUES (1, 'Developer Tools');
INSERT INTO domains (id, name) VALUES (2, 'Artificial Intelligence');

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (1, 'awesome-go', 'google/awesome-go', 'A curated list of awesome Go frameworks', 10000, 500, 50, 1200, true, 1, 1, 1);

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (2, 'prompts.chat', 'facebook/prompts.chat', 'Awesome ChatGPT Prompts', 5000, 200, 10, 600, false, 2, 2, 2);