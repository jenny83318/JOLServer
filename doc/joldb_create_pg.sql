-- Table: public.jol_customer

-- DROP TABLE IF EXISTS public.jol_customer;

CREATE TABLE IF NOT EXISTS public.jol_customer
(
    account character varying(45) COLLATE pg_catalog."default" NOT NULL,
    password character varying(45) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    phone character varying(45) COLLATE pg_catalog."default" NOT NULL,
    name character varying(45) COLLATE pg_catalog."default" NOT NULL,
    city character varying(20) COLLATE pg_catalog."default" NOT NULL,
    district character varying(20) COLLATE pg_catalog."default" NOT NULL,
    address character varying(150) COLLATE pg_catalog."default" NOT NULL,
    status smallint NOT NULL,
    token character varying(100) COLLATE pg_catalog."default" DEFAULT NULL::character varying,
    token_expired character varying(45) COLLATE pg_catalog."default" DEFAULT NULL::character varying,
    CONSTRAINT jol_customer_pkey PRIMARY KEY (account),
    CONSTRAINT jol_customer_email_key UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_customer
    OWNER to admin;


-- Table: public.jol_product

-- DROP TABLE IF EXISTS public.jol_product;

CREATE SEQUENCE jol_product_prodid_seq;
CREATE TABLE IF NOT EXISTS public.jol_product
(
    prodid integer NOT NULL DEFAULT nextval('jol_product_prodid_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    descript character varying(255) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    createdt timestamp with time zone NOT NULL,
    updatedt timestamp with time zone NOT NULL,
    price integer NOT NULL,
    cost integer,
    qty integer,
    category character varying(20) COLLATE pg_catalog."default" NOT NULL,
    imgurl character varying(150) COLLATE pg_catalog."default" DEFAULT NULL::character varying,
    sizeinfo character varying(15) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    series character varying(15) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    CONSTRAINT jol_product_pkey PRIMARY KEY (prodid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_product
    OWNER to admin;



-- Table: public.jol_cart

-- DROP TABLE IF EXISTS public.jol_cart;
CREATE SEQUENCE jol_cart_cartid_seq;
CREATE TABLE IF NOT EXISTS public.jol_cart
(
    cartid integer NOT NULL DEFAULT nextval('jol_cart_cartid_seq'::regclass),
    account character varying(45) COLLATE pg_catalog."default" NOT NULL,
    prodid integer NOT NULL,
    qty integer NOT NULL,
    size character varying(10) COLLATE pg_catalog."default" NOT NULL,
    iscart boolean NOT NULL,
    updatedt timestamp with time zone NOT NULL,
    CONSTRAINT jol_cart_pkey PRIMARY KEY (cartid),
    CONSTRAINT jol_cart_fk0 FOREIGN KEY (prodid)
        REFERENCES public.jol_product (prodid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_cart
    OWNER to admin;


-- Table: public.jol_order

-- DROP TABLE IF EXISTS public.jol_order;
CREATE SEQUENCE jol_order_orderno_seq;
CREATE TABLE IF NOT EXISTS public.jol_order
(
    orderno integer NOT NULL DEFAULT nextval('jol_order_orderno_seq'::regclass),
    ordertime timestamp with time zone NOT NULL,
    account character varying(45) COLLATE pg_catalog."default" NOT NULL,
    totalamt integer NOT NULL,
    status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    deliveryway character varying(20) COLLATE pg_catalog."default" NOT NULL,
    deliveryno character varying(20) COLLATE pg_catalog."default" NOT NULL,
    ordername character varying(20) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    orderphone character varying(20) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    ordercity character varying(10) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    orderdistrict character varying(10) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    orderaddress character varying(150) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    sendname character varying(20) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    sendphone character varying(20) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    sendcity character varying(10) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    senddistrict character varying(10) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    sendaddress character varying(150) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    vehicle character varying(20) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    payby character varying(15) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    vehicletype character varying(15) COLLATE pg_catalog."default" DEFAULT NULL::bpchar,
    updatedt timestamp with time zone NOT NULL,
    CONSTRAINT jol_order_pkey PRIMARY KEY (orderno),
    CONSTRAINT jol_order_fk0 FOREIGN KEY (account)
        REFERENCES public.jol_customer (account) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_order
    OWNER to admin;


-- Table: public.jol_order_detail

-- DROP TABLE IF EXISTS public.jol_order_detail;
CREATE SEQUENCE jol_order_detail_orderdetailno_seq;
CREATE TABLE IF NOT EXISTS public.jol_order_detail
(
    orderdetailno integer NOT NULL DEFAULT nextval('jol_order_detail_orderdetailno_seq'::regclass),
    orderno integer NOT NULL,
    account character varying(45) COLLATE pg_catalog."default" NOT NULL,
    prodid integer NOT NULL,
    qty integer NOT NULL,
    price integer NOT NULL,
    size character varying(10) COLLATE pg_catalog."default" NOT NULL,
    status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    updatedt timestamp with time zone NOT NULL,
    CONSTRAINT jol_order_detail_pkey PRIMARY KEY (orderdetailno),
    CONSTRAINT jol_order_detail_fk1 FOREIGN KEY (prodid)
        REFERENCES public.jol_product (prodid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_order_detail
    OWNER to admin;


-- Table: public.jol_employee

-- DROP TABLE IF EXISTS public.jol_employee;
CREATE SEQUENCE jol_employee_empno_seq;
CREATE TABLE IF NOT EXISTS public.jol_employee
(
    empno integer NOT NULL DEFAULT nextval('jol_employee_empno_seq'::regclass),
    account character varying(45) COLLATE pg_catalog."default" NOT NULL,
    name character varying(45) COLLATE pg_catalog."default" NOT NULL,
    auth integer NOT NULL,
    cruser character varying(45) COLLATE pg_catalog."default" NOT NULL,
    crdt timestamp without time zone NOT NULL,
    upuser character varying(45) COLLATE pg_catalog."default" NOT NULL,
    updt timestamp without time zone NOT NULL,
    phone character varying(20) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    address character varying(150) COLLATE pg_catalog."default" NOT NULL,
    password character varying(45) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT jol_employee_pkey PRIMARY KEY (empno),
    CONSTRAINT jol_employee_account_key UNIQUE (account)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_employee
    OWNER to admin;


-- Table: public.jol_setting

-- DROP TABLE IF EXISTS public.jol_setting;

CREATE TABLE IF NOT EXISTS public.jol_setting
(
    type character varying(45) COLLATE pg_catalog."default" NOT NULL,
    keyname character varying(45) COLLATE pg_catalog."default" NOT NULL,
    value character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(100) COLLATE pg_catalog."default" NOT NULL,
    cruser character varying(45) COLLATE pg_catalog."default" NOT NULL,
    crdt timestamp without time zone NOT NULL,
    upuser character varying(45) COLLATE pg_catalog."default" NOT NULL,
    updt timestamp without time zone NOT NULL,
    CONSTRAINT jol_setting_pkey PRIMARY KEY (type, keyname)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.jol_setting
    OWNER to admin;