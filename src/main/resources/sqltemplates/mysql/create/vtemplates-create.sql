CREATE TABLE `${vtemplates.table}` (
    `${vtemplates.templateid}`    INT           NOT NULL,
    `${vtemplates.template}`      MEDIUMTEXT    NOT NULL,

    PRIMARY KEY                                 (`${vtemplates.templateid}`)
)
DEFAULT CHARSET=utf8mb3;
