# Base de datos

Esta carpeta contiene el script SQL de la base de datos `app_cascos`, pensada para gestionar venta y reventa de cascos, auriculares, AirPods y productos tecnologicos similares.

El archivo principal es:

```text
database/seed.sql
```

## Que crea

El script crea la base de datos `app_cascos` si no existe y define estas 11 tablas:

1. `product_models`
2. `products`
3. `suppliers`
4. `purchases`
5. `purchase_items`
6. `sales`
7. `sale_items`
8. `expenses`
9. `inventory_movements`
10. `profitability_estimations`
11. `settings`

Las tablas usan `utf8mb4`, motor `InnoDB`, claves primarias, claves foraneas, indices utiles, campos `created_at` y `updated_at`, y restricciones basicas para evitar precios, stocks o cantidades invalidas.

## Importar desde terminal

Desde la raiz del proyecto:

```sh
mysql -u root -p < database/seed.sql
```

Si usas otro usuario:

```sh
mysql -u tu_usuario -p < database/seed.sql
```

## Importar desde phpMyAdmin

1. Abre phpMyAdmin.
2. Entra con tu usuario de MySQL o MariaDB.
3. Ve a la pestana `Importar`.
4. Selecciona el archivo `database/seed.sql`.
5. Asegurate de usar codificacion `utf8mb4` si phpMyAdmin lo pregunta.
6. Pulsa `Continuar`.

El script incluye `CREATE DATABASE IF NOT EXISTS app_cascos`, asi que puede ejecutarse desde phpMyAdmin sin crear la base manualmente antes.

## Comprobar las tablas

En MySQL o MariaDB:

```sql
USE app_cascos;
SHOW TABLES;
```

Deberian aparecer exactamente las 11 tablas principales del proyecto.

Para comprobar una tabla concreta:

```sql
DESCRIBE products;
DESCRIBE purchases;
DESCRIBE settings;
```

## Comprobar claves foraneas

Puedes revisar las relaciones con:

```sql
SELECT
  table_name,
  constraint_name,
  column_name,
  referenced_table_name,
  referenced_column_name
FROM information_schema.key_column_usage
WHERE table_schema = 'app_cascos'
  AND referenced_table_name IS NOT NULL
ORDER BY table_name, constraint_name;
```

Tambien puedes revisar el SQL completo de una tabla:

```sql
SHOW CREATE TABLE products;
SHOW CREATE TABLE inventory_movements;
```

## Backup

Para crear una copia de seguridad:

```sh
mysqldump -u root -p app_cascos > app_cascos_backup.sql
```

Para restaurarla en otra base compatible:

```sh
mysql -u root -p app_cascos < app_cascos_backup.sql
```

## Notas importantes

- El script no usa `DROP DATABASE`.
- El script no borra registros existentes.
- Las tablas se crean con `CREATE TABLE IF NOT EXISTS`.
- El dinero se guarda con `DECIMAL`, no con `FLOAT` ni `DOUBLE`.
- Las columnas de configuracion se llaman `setting_key` y `setting_value`; no se usan columnas llamadas `key` o `value`.
- Si una tabla ya existe con una estructura antigua, `CREATE TABLE IF NOT EXISTS` no la modifica. En ese caso conviene revisar una migracion controlada antes de cambiar datos reales.
