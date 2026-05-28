
INSERT INTO languages (id, name) VALUES (1, 'Java');
INSERT INTO languages (id, name) VALUES (2, 'TypeScript');
INSERT INTO languages (id, name) VALUES (3, 'Go');
INSERT INTO languages (id, name) VALUES (4, 'HTML');

INSERT INTO owners (id, login) VALUES (1, 'IlliaMelnyk');
INSERT INTO owners (id, login) VALUES (2, 'mendelu-vova');
INSERT INTO owners (id, login) VALUES (3, 'google');
INSERT INTO owners (id, login) VALUES (4, 'facebook');

INSERT INTO domains (id, name) VALUES (1, 'Web Development');
INSERT INTO domains (id, name) VALUES (2, 'Data Science');
INSERT INTO domains (id, name) VALUES (3, 'Developer Tools');
INSERT INTO domains (id, name) VALUES (4, 'Artificial Intelligence');

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (1, 'snippet-vault', 'IlliaMelnyk/snippet-vault', 'A cool snippet vault project', 100, 10, 2, 5, true, 2, 1, 1);

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (2, 'test-project', 'mendelu-vova/test-project', 'Just a test project', 10, 2, 0, 1, false, 1, 2, 2);

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (3, 'awesome-go', 'google/awesome-go', 'A curated list of awesome Go frameworks', 10000, 500, 50, 1200, true, 3, 3, 3);

INSERT INTO repositories (id, name, full_name, description, stars, forks, issues, watchers, has_wiki, language_id, owner_id, domain_id)
VALUES (4, 'prompts.chat', 'facebook/prompts.chat', 'Awesome ChatGPT Prompts', 5000, 200, 10, 600, false, 4, 4, 4);