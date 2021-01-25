--
-- PostgreSQL database dump
--

-- Dumped from database version 11.5
-- Dumped by pg_dump version 11.5

-- Started on 2020-03-24 02:21:33 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3185 (class 1262 OID 21097)
-- Name: library; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE library WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


ALTER DATABASE library OWNER TO postgres;

\connect library

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 21098)
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    id integer NOT NULL,
    author character varying(255),
    name character varying(255),
    publisher character varying(255)
);


ALTER TABLE public.book OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 21104)
-- Name: copy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.copy (
    id integer NOT NULL,
    available boolean,
    book_id integer
);


ALTER TABLE public.copy OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 21107)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 21109)
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    id integer NOT NULL,
    ended boolean,
    ending_date timestamp without time zone,
    extended boolean,
    last_reminder_email timestamp without time zone,
    starting_date timestamp without time zone,
    status character varying(255),
    tokenuseremail character varying(255),
    tokenuserid character varying(255),
    copy_id integer
);


ALTER TABLE public.loan OWNER TO postgres;

--
-- TOC entry 3176 (class 0 OID 21098)
-- Dependencies: 196
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (id, author, name, publisher) FROM stdin;
2	Axel Allain	Livre deux	Maison édition
3	Axel Allain	Livre trois	Maison édition
4	Axel Allain	Livre quatre	Maison édition
5	Axel Allain	Livre cinq	Maison édition
6	Axel Allain	Livre six	Maison édition
7	Axel Allain	Livre sept	Maison édition
8	Axel Allain	Livre huit	Maison édition
9	Axel Allain	Livre neuf	Maison édition
10	Axel Allain	Livre dix	Maison édition
11	Axel Allain	Livre onze	Maison édition
12	Axel Allain	Livre douze	Maison édition
13	Axel Allain	Livre treize	Maison édition
14	Axel Allain	Livre quatorze	Maison édition
15	Axel Allain	Livre quinze	Maison édition
16	Axel Allain	Livre seize	Maison édition
17	Axel Allain	Livre dix-sept	Maison édition
18	Axel Allain	Livre dix-huit	Maison édition
19	Axel Allain	Livre dix-neuf	Maison édition
20	Axel Allain	Livre vingt	Maison édition
21	Axel Allain	Livre vingt-et-un	Maison édition
22	Axel Allain	Livre vingt-deux	Maison édition
23	Axel Allain	Livre vingt-trois	Maison édition
24	Axel Allain	Livre vingt-quatre	Maison édition
25	Axel Allain	Livre vingt-cinq	Maison édition
1	Axel Allain	Livre test	Maison édition
\.


--
-- TOC entry 3177 (class 0 OID 21104)
-- Dependencies: 197
-- Data for Name: copy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.copy (id, available, book_id) FROM stdin;
4	t	2
5	t	3
6	t	3
3	t	2
1	f	1
2	t	1
\.


--
-- TOC entry 3179 (class 0 OID 21109)
-- Dependencies: 199
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan (id, ended, ending_date, extended, last_reminder_email, starting_date, status, tokenuseremail, tokenuserid, copy_id) FROM stdin;
1	f	2020-04-07 09:00:00	f	\N	2020-03-07 09:00:00	Started	axel.allain.a@gmail.com	58ad44da-f0b6-49f3-8cab-5ac71025810f	1
\.


--
-- TOC entry 3186 (class 0 OID 0)
-- Dependencies: 198
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- TOC entry 3048 (class 2606 OID 21116)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- TOC entry 3050 (class 2606 OID 21118)
-- Name: copy copy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.copy
    ADD CONSTRAINT copy_pkey PRIMARY KEY (id);


--
-- TOC entry 3052 (class 2606 OID 21120)
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (id);


--
-- TOC entry 3053 (class 2606 OID 21121)
-- Name: copy fkof5k7k6c41i06j6fj3slgsmam; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.copy
    ADD CONSTRAINT fkof5k7k6c41i06j6fj3slgsmam FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- TOC entry 3054 (class 2606 OID 21126)
-- Name: loan fkw8dbi4aoljiy817dnmnpaikd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT fkw8dbi4aoljiy817dnmnpaikd FOREIGN KEY (copy_id) REFERENCES public.copy(id);


-- Completed on 2020-03-24 02:21:33 CET

--
-- PostgreSQL database dump complete
--

