INSERT INTO goods (
    id,
    name,
    article,
    description,
    good_category,
    price,
    quantity,
    last_quantity_change,
    created_at
)
SELECT
    uuid_generate_v4(),
    'rail',
    CONCAT('ARTICLE_', gs),
    'test',
    'ELECTRONICS',
    100,
    5,
    '2003-03-25 05:30:30'::timestamp,
    '2003-03-25'::timestamp
FROM
    generate_series(1, 1000000) as gs
LEFT JOIN
    goods ON gs::text = replace(article, 'ARTICLE_', '')::integer::text
WHERE
    goods.id IS NULL;