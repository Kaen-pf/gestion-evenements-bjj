-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Hôte : db
-- Généré le : ven. 01 mai 2026 à 01:53
-- Version du serveur : 12.1.2-MariaDB-ubu2404
-- Version de PHP : 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_bjj`
--

-- --------------------------------------------------------

--
-- Structure de la table `classement`
--

CREATE TABLE `classement` (
  `id` int(11) NOT NULL,
  `id_competiteur` int(11) DEFAULT NULL,
  `id_evenement` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `club`
--

CREATE TABLE `club` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `ville` varchar(100) DEFAULT NULL,
  `responsable` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `combat`
--

CREATE TABLE `combat` (
  `id` int(11) NOT NULL,
  `id_competiteur1` int(11) DEFAULT NULL,
  `id_competiteur2` int(11) DEFAULT NULL,
  `id_evenement` int(11) DEFAULT NULL,
  `resultat` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Déchargement des données de la table `combat`
--

INSERT INTO `combat` (`id`, `id_competiteur1`, `id_competiteur2`, `id_evenement`, `resultat`) VALUES
(1, 14, 43, 3, 'Victoire compétiteur 2'),
(2, 43, 14, 3, 'Victoire du compétiteur 2');

-- --------------------------------------------------------

--
-- Structure de la table `competiteur`
--

CREATE TABLE `competiteur` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `poids` decimal(5,2) DEFAULT NULL,
  `ceinture` varchar(50) DEFAULT NULL,
  `id_club` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Déchargement des données de la table `competiteur`
--

INSERT INTO `competiteur` (`id`, `nom`, `prenom`, `poids`, `ceinture`, `id_club`) VALUES
(1, 'Tama', 'Raimana', 70.50, 'Bleue', NULL),
(2, 'Tetuanui', 'Maeva', 55.00, 'Violette', NULL),
(3, 'Paofai', 'Heimana', 85.00, 'Marron', NULL),
(4, 'Vahine', 'Tearii', 62.00, 'Bleue', NULL),
(5, 'Narii', 'Teva', 94.50, 'Noire', NULL),
(6, 'Farii', 'Moana', 77.00, 'Violette', NULL),
(7, 'Taerea', 'Heiarii', 68.00, 'Marron', NULL),
(8, 'Maitai', 'Teariki', 58.50, 'Bleue', NULL),
(9, 'Temarii', 'Hinanui', 90.00, 'Noire', NULL),
(10, 'Porinetia', 'Taaroa', 73.00, 'Violette', NULL),
(11, 'Hauarii', 'Teanui', 66.00, 'Bleue', NULL),
(12, 'Maruarii', 'Vaihere', 80.00, 'Marron', NULL),
(13, 'Taravao', 'Heimana', 57.50, 'Blanche', NULL),
(14, 'Faatau', 'Rautea', 92.00, 'Noire', NULL),
(15, 'Tearanui', 'Moana', 71.00, 'Bleue', NULL),
(16, 'Paea', 'Teuruarii', 63.50, 'Violette', NULL),
(17, 'Hitimana', 'Tamatoa', 88.00, 'Marron', NULL),
(18, 'Vaihonu', 'Heimata', 54.00, 'Bleue', NULL),
(19, 'Tutearii', 'Reva', 76.00, 'Violette', NULL),
(20, 'Manutea', 'Tearoha', 69.00, 'Bleue', NULL),
(21, 'Heiani', 'Taufa', 83.50, 'Marron', NULL),
(22, 'Purearii', 'Moemoea', 60.00, 'Bleue', NULL),
(23, 'Tautiare', 'Heimoana', 95.00, 'Noire', NULL),
(24, 'Vaitiare', 'Teareva', 65.00, 'Violette', NULL),
(25, 'Rautea', 'Tematai', 78.00, 'Marron', NULL),
(26, 'Teahupo', 'Heimana', 87.50, 'Noire', NULL),
(27, 'Farearii', 'Teuruarii', 56.00, 'Bleue', NULL),
(28, 'Nahearii', 'Moana', 72.00, 'Violette', NULL),
(29, 'Tauarii', 'Heimoana', 64.50, 'Bleue', NULL),
(30, 'Maitearii', 'Teariki', 91.00, 'Noire', NULL),
(31, 'Punaauia', 'Teva', 74.00, 'Marron', NULL),
(32, 'Teareva', 'Heimata', 59.00, 'Bleue', NULL),
(33, 'Haapiti', 'Raimana', 82.00, 'Violette', NULL),
(34, 'Tefarerii', 'Moemoea', 67.50, 'Bleue', NULL),
(35, 'Vaiarii', 'Taufa', 96.00, 'Noire', NULL),
(36, 'Moearii', 'Tearoha', 53.00, 'Blanche', NULL),
(37, 'Tipaerui', 'Heimana', 79.00, 'Marron', NULL),
(38, 'Raiatea', 'Tematai', 61.00, 'Bleue', NULL),
(39, 'Huahine', 'Moana', 86.00, 'Noire', NULL),
(40, 'Bora', 'Teareva', 70.00, 'Violette', NULL),
(41, 'Maupiti', 'Teuruarii', 55.50, 'Bleue', NULL),
(42, 'Rangiroa', 'Heimoana', 93.00, 'Noire', NULL),
(43, 'Fakarava', 'Raimana', 68.50, 'Marron', NULL),
(44, 'Tikehau', 'Moemoea', 75.00, 'Violette', NULL),
(45, 'Manihi', 'Teariki', 84.00, 'Marron', NULL),
(46, 'Takaroa', 'Teva', 58.00, 'Bleue', NULL),
(47, 'Makemo', 'Heimata', 89.00, 'Noire', NULL),
(48, 'Hao', 'Tearoha', 63.00, 'Bleue', NULL),
(49, 'Tureia', 'Taufa', 77.50, 'Violette', NULL),
(50, 'Reao', 'Moana', 71.50, 'Bleue', NULL),
(51, 'Pukapuka', 'Rautea', 94.00, 'Noire', NULL),
(52, 'Nassau', 'Tematai', 66.50, 'Marron', NULL),
(53, 'Suwarrow', 'Heimana', 81.00, 'Violette', NULL),
(54, 'Marutea', 'Teareva', 57.00, 'Bleue', NULL),
(55, 'Temoe', 'Teuruarii', 87.00, 'Noire', NULL),
(56, 'Vahitahi', 'Moemoea', 62.50, 'Bleue', NULL),
(57, 'Nukutavake', 'Raimana', 76.50, 'Marron', NULL),
(58, 'Pinaki', 'Heimoana', 54.50, 'Blanche', NULL),
(59, 'Vairaatea', 'Teariki', 92.50, 'Noire', NULL),
(60, 'Tematangi', 'Teva', 69.50, 'Violette', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `evenement`
--

CREATE TABLE `evenement` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `date_evenement` date DEFAULT NULL,
  `lieu` varchar(100) DEFAULT NULL,
  `id_sport` int(11) DEFAULT NULL,
  `nb_participants_max` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Déchargement des données de la table `evenement`
--

INSERT INTO `evenement` (`id`, `nom`, `date_evenement`, `lieu`, `id_sport`, `nb_participants_max`) VALUES
(3, 'Tournois MMA Tahiti 2027', '2026-04-25', 'Mahina', 2, 30);

-- --------------------------------------------------------

--
-- Structure de la table `inscription`
--

CREATE TABLE `inscription` (
  `id` int(11) NOT NULL,
  `id_competiteur` int(11) DEFAULT NULL,
  `id_evenement` int(11) DEFAULT NULL,
  `categorie` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `sport`
--

CREATE TABLE `sport` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Déchargement des données de la table `sport`
--

INSERT INTO `sport` (`id`, `nom`) VALUES
(2, 'MMA');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `login` varchar(100) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `role` enum('admin','organisateur') NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `login`, `mot_de_passe`, `role`, `nom`, `prenom`) VALUES
(1, 'admin', 'admin123', 'admin', 'Admin', 'Super'),
(2, 'Manu', 'luia', 'organisateur', 'Manu', 'Fts');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `classement`
--
ALTER TABLE `classement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_competiteur` (`id_competiteur`),
  ADD KEY `id_evenement` (`id_evenement`);

--
-- Index pour la table `club`
--
ALTER TABLE `club`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `combat`
--
ALTER TABLE `combat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_competiteur1` (`id_competiteur1`),
  ADD KEY `id_competiteur2` (`id_competiteur2`),
  ADD KEY `id_evenement` (`id_evenement`);

--
-- Index pour la table `competiteur`
--
ALTER TABLE `competiteur`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_club` (`id_club`);

--
-- Index pour la table `evenement`
--
ALTER TABLE `evenement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_sport` (`id_sport`);

--
-- Index pour la table `inscription`
--
ALTER TABLE `inscription`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_competiteur` (`id_competiteur`),
  ADD KEY `id_evenement` (`id_evenement`);

--
-- Index pour la table `sport`
--
ALTER TABLE `sport`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `classement`
--
ALTER TABLE `classement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `club`
--
ALTER TABLE `club`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `combat`
--
ALTER TABLE `combat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `competiteur`
--
ALTER TABLE `competiteur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT pour la table `evenement`
--
ALTER TABLE `evenement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `inscription`
--
ALTER TABLE `inscription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `sport`
--
ALTER TABLE `sport`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `classement`
--
ALTER TABLE `classement`
  ADD CONSTRAINT `1` FOREIGN KEY (`id_competiteur`) REFERENCES `competiteur` (`id`),
  ADD CONSTRAINT `2` FOREIGN KEY (`id_evenement`) REFERENCES `evenement` (`id`);

--
-- Contraintes pour la table `combat`
--
ALTER TABLE `combat`
  ADD CONSTRAINT `1` FOREIGN KEY (`id_competiteur1`) REFERENCES `competiteur` (`id`),
  ADD CONSTRAINT `2` FOREIGN KEY (`id_competiteur2`) REFERENCES `competiteur` (`id`),
  ADD CONSTRAINT `3` FOREIGN KEY (`id_evenement`) REFERENCES `evenement` (`id`);

--
-- Contraintes pour la table `competiteur`
--
ALTER TABLE `competiteur`
  ADD CONSTRAINT `1` FOREIGN KEY (`id_club`) REFERENCES `club` (`id`);

--
-- Contraintes pour la table `evenement`
--
ALTER TABLE `evenement`
  ADD CONSTRAINT `1` FOREIGN KEY (`id_sport`) REFERENCES `sport` (`id`);

--
-- Contraintes pour la table `inscription`
--
ALTER TABLE `inscription`
  ADD CONSTRAINT `1` FOREIGN KEY (`id_competiteur`) REFERENCES `competiteur` (`id`),
  ADD CONSTRAINT `2` FOREIGN KEY (`id_evenement`) REFERENCES `evenement` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
