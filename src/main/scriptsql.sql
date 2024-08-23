CREATE TABLE ingredient(
                           id_ingredient SERIAL PRIMARY KEY,
                           label_ingredient VARCHAR(50) ,
                           PRIMARY KEY(id_ingredient)
);

CREATE TABLE categorie(
                          id_categorie SERIAL PRIMARY KEY,
                          label_categorie VARCHAR(50) ,
                          PRIMARY KEY(id_categorie)
);

CREATE TABLE recette(
                        id_recette SERIAL PRIMARY KEY,
                        label_recette VARCHAR(50) ,
                        temps_preparation INTEGER,
                        temps_cuisson INTEGER,
                        difficulte VARCHAR(50) ,
                        id_categorie INTEGER NOT NULL,
                        PRIMARY KEY(id_recette),
                        FOREIGN KEY(id_categorie) REFERENCES categorie(id_categorie)
);

CREATE TABLE etape(
                      id_etape SERIAL PRIMARY KEY,
                      description_etape VARCHAR(50) ,
                      id_recette INTEGER NOT NULL,
                      PRIMARY KEY(id_etape),
                      FOREIGN KEY(id_recette) REFERENCES recette(id_recette)
);

CREATE TABLE commentaire(
                            id_commentaire SERIAL PRIMARY KEY,
                            description_commentaire VARCHAR(50) ,
                            id_recette INTEGER NOT NULL,
                            PRIMARY KEY(id_commentaire),
                            FOREIGN KEY(id_recette) REFERENCES recette(id_recette)
);

CREATE TABLE composer(
                         id_ingredient INTEGER,
                         id_recette INTEGER,
                         PRIMARY KEY(id_ingredient, id_recette),
                         FOREIGN KEY(id_ingredient) REFERENCES ingredient(id_ingredient),
                         FOREIGN KEY(id_recette) REFERENCES recette(id_recette)
);
