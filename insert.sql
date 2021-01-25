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

--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.book (id, author, name, publisher) VALUES (2, 'Axel Allain', 'Livre deux', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (3, 'Axel Allain', 'Livre trois', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (4, 'Axel Allain', 'Livre quatre', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (5, 'Axel Allain', 'Livre cinq', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (6, 'Axel Allain', 'Livre six', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (7, 'Axel Allain', 'Livre sept', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (8, 'Axel Allain', 'Livre huit', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (9, 'Axel Allain', 'Livre neuf', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (10, 'Axel Allain', 'Livre dix', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (11, 'Axel Allain', 'Livre onze', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (12, 'Axel Allain', 'Livre douze', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (13, 'Axel Allain', 'Livre treize', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (14, 'Axel Allain', 'Livre quatorze', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (15, 'Axel Allain', 'Livre quinze', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (16, 'Axel Allain', 'Livre seize', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (17, 'Axel Allain', 'Livre dix-sept', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (18, 'Axel Allain', 'Livre dix-huit', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (19, 'Axel Allain', 'Livre dix-neuf', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (20, 'Axel Allain', 'Livre vingt', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (21, 'Axel Allain', 'Livre vingt-et-un', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (22, 'Axel Allain', 'Livre vingt-deux', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (23, 'Axel Allain', 'Livre vingt-trois', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (24, 'Axel Allain', 'Livre vingt-quatre', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (25, 'Axel Allain', 'Livre vingt-cinq', 'Maison édition');
INSERT INTO public.book (id, author, name, publisher) VALUES (1, 'Axel Allain', 'Livre test', 'Maison édition');


--
-- Data for Name: copy; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.copy (id, available, book_id) VALUES (4, true, 2);
INSERT INTO public.copy (id, available, book_id) VALUES (5, true, 3);
INSERT INTO public.copy (id, available, book_id) VALUES (6, true, 3);
INSERT INTO public.copy (id, available, book_id) VALUES (3, true, 2);
INSERT INTO public.copy (id, available, book_id) VALUES (1, false, 1);
INSERT INTO public.copy (id, available, book_id) VALUES (2, true, 1);


--
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.loan (id, ended, ending_date, extended, last_reminder_email, starting_date, status, tokenuseremail, tokenuserid, copy_id) VALUES (1, false, '2020-04-07 09:00:00', false, NULL, '2020-03-07 09:00:00', 'Started', 'axel.allain.a@gmail.com', '58ad44da-f0b6-49f3-8cab-5ac71025810f', 1);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- PostgreSQL database dump complete
--

