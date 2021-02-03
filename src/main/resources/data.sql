-- Volcado de datos para la tabla `roles`
INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MANAGER'),
(3, 'ROLE_OPERATOR'),
(4, 'ROLE_CUSTOMER'),
(5, 'ROLE_AUTHENTICATED');

-- Volcado de datos para la tabla `users`
INSERT INTO `users` (`id`, `address`, `city`, `country`, `dni`, `email`, `first_access`, `first_name`, `is_account_non_expired`, `is_account_non_locked`, `is_credentials_non_expired`, `is_enabled`, `last_access`, `last_name`, `password`, `phone`, `username`) VALUES
(1, NULL, NULL, NULL, NULL, 'adminmanager@sip.es', NULL, NULL, b'1', b'1', b'1', b'1', NULL, NULL, '$2a$10$3KK4.jlsbG.B9U3sWIv5GukwiEMjQF8Jh2f0ueCJmbhYxybiXwWM.', NULL, 'AdminManager'),
(2, NULL, NULL, NULL, NULL, 'manager@sip.es', NULL, NULL, b'1', b'1', b'1', b'1', NULL, NULL, '$2a$10$Rqsda5/VUncCAWHNVcXM6uj3X/WhLs5evew2G8REUn.1q8MFiMbV2', NULL, 'manager'),
(3, NULL, NULL, NULL, NULL, 'admin@sip.es', NULL, NULL, b'1', b'1', b'1', b'1', NULL, NULL, '$2a$10$TyCW3ognRIuq0bM0JRQUu.kUL3Ch8ckOrNmEGbjj5m1SssnzGHzhC', NULL, 'admin');

-- Volcado de datos para la tabla `user_roles`
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(3, 1),
(1, 2),
(2, 2);