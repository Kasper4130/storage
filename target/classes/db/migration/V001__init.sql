CREATE TABLE IF NOT EXISTS public.goods
(
    id uuid NOT NULL,
    name character varying NOT NULL,
    article character varying NOT NULL,
    description text,
    good_category character varying NOT NULL,
    price numeric NOT NULL,
    quantity integer NOT NULL,
    last_quantity_change timestamp with time zone,
    created_at timestamp with time zone NOT NULL,
    CONSTRAINT good_pkey PRIMARY KEY (id)
);