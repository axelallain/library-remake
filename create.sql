--
-- PostgreSQL database dump
--

-- Dumped from database version 11.5
-- Dumped by pg_dump version 11.5

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
-- Name: copy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.copy (
    id integer NOT NULL,
    available boolean,
    book_id integer
);


ALTER TABLE public.copy OWNER TO postgres;

--
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
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: copy copy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.copy
    ADD CONSTRAINT copy_pkey PRIMARY KEY (id);


--
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (id);


--
-- Name: copy fkof5k7k6c41i06j6fj3slgsmam; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.copy
    ADD CONSTRAINT fkof5k7k6c41i06j6fj3slgsmam FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: loan fkw8dbi4aoljiy817dnmnpaikd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT fkw8dbi4aoljiy817dnmnpaikd FOREIGN KEY (copy_id) REFERENCES public.copy(id);


--
-- PostgreSQL database dump complete
--

